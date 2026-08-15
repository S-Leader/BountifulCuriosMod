package keletu.bountifulcurios.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import keletu.bountifulcurios.registry.ModItems;
import keletu.bountifulcurios.registry.ModLootModifiers;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public final class OriginalLootModifier extends LootModifier {
    public static final Codec<OriginalLootModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance).and(Codec.STRING.fieldOf("pool")
                            .forGetter(modifier -> modifier.pool))
                    .apply(instance, OriginalLootModifier::new));

    private final String pool;

    public OriginalLootModifier(LootItemCondition[] conditions, String pool) {
        super(conditions);
        this.pool = pool;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot,
                                                 LootContext context) {
        RandomSource random = context.getRandom();
        switch (pool) {
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
    public Codec<? extends IGlobalLootModifier> codec() {
        return ModLootModifiers.ORIGINAL_LOOT.get();
    }
}
