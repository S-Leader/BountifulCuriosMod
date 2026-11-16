package keletu.bountifulcurios.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import keletu.bountifulcurios.LootConfig;
import keletu.bountifulcurios.registry.ModBlocks;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import keletu.bountifulcurios.registry.ModItems;
import keletu.bountifulcurios.registry.ModLootModifiers;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public final class OriginalLootModifier extends LootModifier {
    public static final MapCodec<OriginalLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance).and(Codec.STRING.fieldOf("pool")
                            .forGetter(modifier -> modifier.pool))
                    .apply(instance, OriginalLootModifier::new));

    private final String pool;

    public OriginalLootModifier(LootItemCondition[] conditions, int priority, String pool) {
        super(conditions, priority);
        this.pool = pool;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot,
                                                 LootContext context) {
        RandomSource random = context.getRandom();
        switch (pool) {
            case "bogged" -> { if (random.nextDouble() < LootConfig.BOGGED_BEZOAR.get()) add(loot, ModItems.BEZOAR.get()); }
            case "parched" -> { if (random.nextDouble() < LootConfig.PARCHED_APPLE.get()) add(loot, ModItems.APPLE.get()); }
            case "warden" -> { if (random.nextDouble() < LootConfig.WARDEN_VITAMIN_A.get()) add(loot, ModItems.VITAMINS_A.get()); }
            case "ominous_trial_spawner" -> addTrialSpawnerLoot(loot, random);
            case "ominous_vault" -> addOminousVaultLoot(loot, random);
            case "normal_vault" -> addNormalVaultLoot(loot, random);
            case "husk" -> add(loot, ModItems.APPLE.get());
            case "elder_guardian" -> add(loot, ModItems.VITAMINS.get());
            case "stray" -> add(loot, ModItems.RING_OVERCLOCKING.get());
            case "shulker" -> add(loot, ModItems.SHULKER_HEART.get());
            case "cave_spider" -> add(loot, ModItems.BEZOAR.get());
            case "ender_dragon" -> add(loot, ModItems.ENDER_DRAGON_SCALE.get(),
                    3 + random.nextInt(4));
            case "ancient_city_vitamin_a" -> add(loot, ModItems.VITAMINS_A.get());
            case "dungeon_base" -> addDungeonBase(loot, random);
            case "dungeon_potions" -> addDungeonPotions(loot, random);
            case "nether_base" -> addNetherBase(loot, random);
            case "nether_misc" -> addNetherMisc(loot, random);
            default -> throw new IllegalStateException("Unknown Bountiful Curios loot pool: "
                    + pool);
        }
        return loot;
    }

    private static void addTrialSpawnerLoot(ObjectArrayList<ItemStack> loot, RandomSource random) {
        TrialRewardContext context = TrialRewardContext.current();
        if (context == null || !context.ominous()) return;
        Item reward = switch (context.mob()) {
            case "minecraft:stray" -> ModItems.RING_OVERCLOCKING.get();
            case "minecraft:husk" -> ModItems.APPLE.get();
            case "minecraft:cave_spider", "minecraft:bogged" -> ModItems.BEZOAR.get();
            default -> null;
        };
        if (reward != null && random.nextDouble() < LootConfig.OMINOUS_SPAWNER.get()) add(loot, reward);
    }

    private static void addOminousVaultLoot(ObjectArrayList<ItemStack> loot, RandomSource random) {
        if (random.nextDouble() < LootConfig.OMINOUS_BASE.get()) {
            Item[] base = {ModItems.VITAMINS_A.get(), ModItems.VITAMINS.get(), ModItems.BEZOAR.get(), ModItems.APPLE.get(), ModItems.RING_OVERCLOCKING.get(), ModItems.SHULKER_HEART.get(), ModItems.SUNGLASSES.get(), ModItems.ENDER_DRAGON_SCALE.get()};
            add(loot, base[random.nextInt(base.length)]);
        }
        if (random.nextDouble() < LootConfig.OMINOUS_CANDLE.get()) add(loot, ModBlocks.WATER_CANDLE_ITEM.get());
        if (random.nextDouble() < LootConfig.OMINOUS_SILT.get()) add(loot, ModItems.SPECTRAL_SILT.get(), 1 + random.nextInt(5));
    }

    private static void addNormalVaultLoot(ObjectArrayList<ItemStack> loot, RandomSource random) {
        if (random.nextDouble() >= LootConfig.NORMAL_VAULT.get()) return;
        switch (random.nextInt(9)) {
            case 0 -> add(loot, ModItems.SHIELD_COBALT.get());
            case 1 -> add(loot, ModItems.AMULET_SIN_EMPTY.get());
            case 2 -> add(loot, ModItems.CROWN_GOLD.get());
            case 3 -> add(loot, ModItems.MAGIC_MIRROR.get());
            case 4 -> add(loot, ModItems.OBSIDIAN_SKULL.get());
            case 5 -> add(loot, ModItems.POTION_RECALL.get(), 1 + random.nextInt(3));
            case 6 -> add(loot, ModItems.POTION_WORMHOLE.get(), 1 + random.nextInt(2));
            case 7 -> add(loot, ModItems.LUCKY_HORSESHOE.get());
            default -> add(loot, ModItems.BALLOON.get());
        }
    }

    private static void addDungeonBase(ObjectArrayList<ItemStack> loot, RandomSource random) {
        int roll = random.nextInt(87);
        if (roll < 10) {
            add(loot, ModItems.BALLOON.get());
        } else if (roll < 20) {
            add(loot, ModItems.SHIELD_COBALT.get());
        } else if (roll < 30) {
            add(loot, ModItems.MAGIC_MIRROR.get());
        } else if (roll < 40) {
            add(loot, ModItems.LUCKY_HORSESHOE.get());
        } else if (roll < 50) {
            add(loot, ModItems.BROKEN_HEART.get());
        } else if (roll < 60) {
            add(loot, ModItems.SUNGLASSES.get());
        } else if (roll < 70) {
            add(loot, ModItems.AMULET_CROSS.get());
        } else if (roll < 73) {
            add(loot, ModItems.BROKEN_BLACK_DRAGON_SCALE.get());
        } else if (roll < 76) {
            add(loot, ModItems.AMULET_SIN_EMPTY.get());
        } else if (roll < 77) {
            add(loot, ModItems.PHANTOM_PRISM.get());
        } else {
            add(loot, ModItems.FLARE_GUN.get());
            add(loot, ModItems.FLARE_RED.get(), 25 + random.nextInt(24));
        }
    }

    private static void addDungeonPotions(ObjectArrayList<ItemStack> loot,
                                          RandomSource random) {
        int rolls = 1 + random.nextInt(6);
        for (int i = 0; i < rolls; i++) {
            int roll = random.nextInt(100);
            if (roll < 50) {
                add(loot, ModItems.POTION_RECALL.get());
            } else if (roll < 75) {
                add(loot, ModItems.POTION_WORMHOLE.get());
            }
        }
    }

    private static void addNetherBase(ObjectArrayList<ItemStack> loot, RandomSource random) {
        int roll = random.nextInt(51);
        if (roll < 10) {
            add(loot, ModItems.BROKEN_BLACK_DRAGON_SCALE.get());
        } else if (roll < 20) {
            add(loot, ModItems.MAGIC_MIRROR.get());
        } else if (roll < 30) {
            add(loot, ModItems.OBSIDIAN_SKULL.get());
        } else if (roll < 40) {
            add(loot, ModItems.BROKEN_HEART.get());
        } else if (roll < 50) {
            add(loot, ModItems.AMULET_SIN_EMPTY.get());
        } else {
            add(loot, ModItems.PHANTOM_PRISM.get());
        }
    }

    private static void addNetherMisc(ObjectArrayList<ItemStack> loot, RandomSource random) {
        int roll = random.nextInt(55);
        if (roll < 5) {
            add(loot, ModItems.POTION_RECALL.get());
        } else if (roll < 30) {
            add(loot, ModItems.POTION_WORMHOLE.get());
        } else {
            add(loot, ModItems.IRON_RING.get());
        }
    }

    private static void add(ObjectArrayList<ItemStack> loot, Item item) {
        add(loot, item, 1);
    }

    private static void add(ObjectArrayList<ItemStack> loot, Item item, int count) {
        loot.add(new ItemStack(item, count));
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return ModLootModifiers.ORIGINAL_LOOT.get();
    }
}
