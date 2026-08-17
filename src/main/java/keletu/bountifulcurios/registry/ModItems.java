package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.item.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;
import java.util.function.Supplier;

public final class ModItems {
    private static final UUID KNOCKBACK_UUID = UUID.fromString("9016ba1d-70dd-46c4-b0b4-fc4ea39886c1");

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BountifulCurios.MODID);

    public static final RegistryObject<Item> SUNGLASSES = bauble("sunglasses", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.BLINDNESS));
    public static final RegistryObject<Item> APPLE = bauble("apple", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.HUNGER, MobEffects.CONFUSION));
    public static final RegistryObject<Item> VITAMINS = bauble("vitamins", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN));
    public static final RegistryObject<Item> VITAMINS_A = bauble("vitamins_a", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.DARKNESS));
    public static final RegistryObject<Item> VITAMINS_ALPHA = bauble("vitamins_alpha", () -> (Item) new BaubleItem(epic()).immuneTo(MobEffects.DARKNESS, MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN));
    public static final RegistryObject<Item> RING_OVERCLOCKING = bauble("ring_overclocking", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.MOVEMENT_SLOWDOWN).withAttribute(Attributes.MOVEMENT_SPEED, uuid("067d9c52-5ffb-4fad-b581-f17ecc799549"), "Ring of Overclocking speed", 0.07D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Item> SHULKER_HEART = bauble("shulker_heart", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.LEVITATION));
    public static final RegistryObject<Item> RING_FREE_ACTION = bauble("ring_free_action", () -> (Item) new BaubleItem(epic()).immuneTo(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.LEVITATION));
    public static final RegistryObject<Item> BEZOAR = bauble("bezoar", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.POISON));

    public static final RegistryObject<Item> ENDER_DRAGON_SCALE = plain("ender_dragon_scale");
    public static final RegistryObject<Item> BROKEN_BLACK_DRAGON_SCALE = plain("broken_black_dragon_scale");
    public static final RegistryObject<Item> BLACK_DRAGON_SCALE = bauble("black_dragon_scale", () -> (Item) new BaubleItem(rare()).immuneTo(MobEffects.WITHER));
    public static final RegistryObject<Item> MIXED_DRAGON_SCALE = bauble("mixed_dragon_scale", () -> (Item) new BaubleItem(epic()).immuneTo(MobEffects.POISON, MobEffects.WITHER));
    public static final RegistryObject<Item> ANKH_CHARM = bauble("ankh_charm", () -> (Item) new BaubleItem(epic()).immuneTo(MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DARKNESS, MobEffects.HUNGER, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER));
    public static final RegistryObject<Item> OBSIDIAN_SKULL = bauble("obsidian_skull", () -> new BaubleItem(rare().fireResistant()));

    public static final RegistryObject<Item> IRON_RING = plain("iron_ring");
    public static final RegistryObject<Item> RING_FLYWHEEL = ITEMS.register("ring_flywheel", () -> new FlywheelRingItem(rare(), 1_600_000));
    public static final RegistryObject<Item> RING_FLYWHEEL_ADVANCED = ITEMS.register("ring_flywheel_advanced", () -> new FlywheelRingItem(epic(), 8_000_000));

    public static final RegistryObject<Item> SHIELD_COBALT = ITEMS.register("shield_cobalt", () -> (Item) new BaubleShieldItem(new Item.Properties().durability(1008).rarity(Rarity.RARE), Items.IRON_INGOT).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Item> SHIELD_OBSIDIAN = ITEMS.register("shield_obsidian", () -> (Item) new BaubleShieldItem(new Item.Properties().durability(1344).rarity(Rarity.RARE).fireResistant(), Items.OBSIDIAN).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Item> SHIELD_ANKH = ITEMS.register("shield_ankh", () -> (Item) new BaubleShieldItem(new Item.Properties().durability(1680).rarity(Rarity.EPIC).fireResistant(), Items.OBSIDIAN).immuneTo(MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DARKNESS, MobEffects.HUNGER, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Item> MAGIC_MIRROR = ITEMS.register("magic_mirror", () -> new MirrorItem(rare(), MirrorItem.Destination.SPAWN, true));
    public static final RegistryObject<Item> POTION_RECALL = ITEMS.register("potion_recall", () -> new MirrorItem(new Item.Properties().rarity(Rarity.UNCOMMON), MirrorItem.Destination.SPAWN, false));
    public static final RegistryObject<Item> WORMHOLE_MIRROR = ITEMS.register("wormhole_mirror", () -> new MirrorItem(epic(), MirrorItem.Destination.SPAWN_OR_OTHER_WHEN_CROUCHING, true));
    public static final RegistryObject<Item> POTION_WORMHOLE = ITEMS.register("potion_wormhole", () -> new MirrorItem(new Item.Properties().rarity(Rarity.UNCOMMON), MirrorItem.Destination.OTHER_PLAYER, false));

    public static final RegistryObject<Item> BALLOON = bauble("balloon", () -> new BaubleItem(rare()));
    public static final RegistryObject<Item> LUCKY_HORSESHOE = bauble("lucky_horseshoe", () -> new BaubleItem(rare()));
    public static final RegistryObject<Item> HORSESHOE_BALLOON = bauble("horseshoe_balloon", () -> new BaubleItem(epic()));

    public static final RegistryObject<Item> AMULET_SIN_EMPTY = bauble("amulet_sin_empty", () -> new BaubleItem(new Item.Properties()));
    public static final RegistryObject<Item> AMULET_SIN_GLUTTONY = bauble("amulet_sin_gluttony", () -> new BaubleItem(epic()));
    public static final RegistryObject<Item> AMULET_SIN_PRIDE = bauble("amulet_sin_pride", () -> (Item) new BaubleItem(epic()).withAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get(), uuid("6886f79c-5fb4-4f1c-b6bf-0ab66163e864"), "Pride Pendant step height", 0.64993D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Item> CROWN_GOLD = ITEMS.register("crown_gold", () -> new CrownItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> AMULET_SIN_WRATH = bauble("amulet_sin_wrath", () -> (Item) new BaubleItem(epic()).withAttribute(Attributes.ATTACK_DAMAGE, uuid("2d75d7e2-38bb-465e-a2b1-8a59c552fe40"), "Wrath Pendant damage", 2.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Item> BROKEN_HEART = bauble("broken_heart", () -> new BaubleItem(epic()));
    public static final RegistryObject<Item> AMULET_CROSS = bauble("amulet_cross", () -> new BaubleItem(rare()));

    public static final RegistryObject<Item> PHANTOM_PRISM = ITEMS.register("phantom_prism", () -> new PhantomPrismItem(epic()));
    public static final RegistryObject<Item> DISINTEGRATION_TABLET = ITEMS.register("disintegration_tablet", () -> new ReusableCraftingItem(rare()));
    public static final RegistryObject<Item> SPECTRAL_SILT = ITEMS.register("spectral_silt", () -> new GlintItem(new Item.Properties()));
    public static final RegistryObject<Item> RESPLENDENT_TOKEN = plain("resplendent_token");
    public static final RegistryObject<Item> FLARE_RED = ITEMS.register("flare_red", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLARE_GUN = ITEMS.register("flare_gun", () -> new FlareGunItem(rare()));

    private static RegistryObject<Item> bauble(String id, Supplier<? extends Item> supplier) {
        return ITEMS.register(id, supplier);
    }

    private static RegistryObject<Item> plain(String id) {
        return ITEMS.register(id, () -> new BCItem(new Item.Properties()));
    }

    private static Item.Properties rare() {
        return new Item.Properties().rarity(Rarity.RARE);
    }

    private static Item.Properties epic() {
        return new Item.Properties().rarity(Rarity.EPIC);
    }

    private static UUID uuid(String id) {
        return UUID.fromString(id);
    }

    private ModItems() {
    }
}
