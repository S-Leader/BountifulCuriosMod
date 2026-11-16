package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.item.BaubleItem;
import keletu.bountifulcurios.item.BaubleShieldItem;
import keletu.bountifulcurios.item.BCItem;
import keletu.bountifulcurios.item.CrownItem;
import keletu.bountifulcurios.item.FlareGunItem;
import keletu.bountifulcurios.item.FlywheelRingItem;
import keletu.bountifulcurios.item.GlintItem;
import keletu.bountifulcurios.item.MirrorItem;
import keletu.bountifulcurios.item.PhantomPrismItem;
import keletu.bountifulcurios.item.ReusableCraftingItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import net.neoforged.neoforge.registries.DeferredRegister;

import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
import keletu.bountifulcurios.item.EquippedItems;



public final class ModItems {
    private static final Identifier KNOCKBACK_UUID = uuid("9016ba1d-70dd-46c4-b0b4-fc4ea39886c1");

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BountifulCurios.MODID);

    public static final DeferredItem<Item> SUNGLASSES = bauble("sunglasses", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.BLINDNESS));
    public static final DeferredItem<Item> APPLE = bauble("apple", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.HUNGER, MobEffects.NAUSEA));
    public static final DeferredItem<Item> VITAMINS = bauble("vitamins", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.WEAKNESS, MobEffects.MINING_FATIGUE));
    public static final DeferredItem<Item> VITAMINS_A = bauble("vitamins_a", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.DARKNESS));
    public static final DeferredItem<Item> VITAMINS_ALPHA = bauble("vitamins_alpha", properties -> (Item) new BaubleItem(epic(properties)).immuneTo(MobEffects.DARKNESS, MobEffects.WEAKNESS, MobEffects.MINING_FATIGUE));
    public static final DeferredItem<Item> RING_OVERCLOCKING = bauble("ring_overclocking", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.SLOWNESS).withAttribute(Attributes.MOVEMENT_SPEED, uuid("067d9c52-5ffb-4fad-b581-f17ecc799549"), "Ring of Overclocking speed", 0.07D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredItem<Item> SHULKER_HEART = bauble("shulker_heart", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.LEVITATION));
    public static final DeferredItem<Item> RING_FREE_ACTION = bauble("ring_free_action", properties -> (Item) new BaubleItem(epic(properties)).immuneTo(MobEffects.SLOWNESS, MobEffects.LEVITATION));
    public static final DeferredItem<Item> BEZOAR = bauble("bezoar", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.POISON));

    public static final DeferredItem<Item> ENDER_DRAGON_SCALE = plain("ender_dragon_scale");
    public static final DeferredItem<Item> BROKEN_BLACK_DRAGON_SCALE = plain("broken_black_dragon_scale");
    public static final DeferredItem<Item> BLACK_DRAGON_SCALE = bauble("black_dragon_scale", properties -> (Item) new BaubleItem(rare(properties)).immuneTo(MobEffects.WITHER));
    public static final DeferredItem<Item> MIXED_DRAGON_SCALE = bauble("mixed_dragon_scale", properties -> (Item) new BaubleItem(epic(properties)).immuneTo(MobEffects.POISON, MobEffects.WITHER));
    public static final DeferredItem<Item> ANKH_CHARM = bauble("ankh_charm", properties -> (Item) new BaubleItem(epic(properties)).immuneTo(MobEffects.BLINDNESS, MobEffects.NAUSEA, MobEffects.DARKNESS, MobEffects.HUNGER, MobEffects.MINING_FATIGUE, MobEffects.WEAKNESS, MobEffects.SLOWNESS, MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER));
    public static final DeferredItem<Item> OBSIDIAN_SKULL = bauble("obsidian_skull", properties -> new BaubleItem(rare(properties).fireResistant()));

    public static final DeferredItem<Item> IRON_RING = plain("iron_ring");
    public static final DeferredItem<Item> RING_FLYWHEEL = ITEMS.registerItem("ring_flywheel", properties -> new FlywheelRingItem(rare(properties), 1_600_000));
    public static final DeferredItem<Item> RING_FLYWHEEL_ADVANCED = ITEMS.registerItem("ring_flywheel_advanced", properties -> new FlywheelRingItem(epic(properties), 8_000_000));

    public static final DeferredItem<Item> SHIELD_COBALT = ITEMS.registerItem("shield_cobalt", properties -> (Item) new BaubleShieldItem(EquippedItems.shield(properties).durability(1008).rarity(Rarity.RARE), Items.IRON_INGOT).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredItem<Item> SHIELD_OBSIDIAN = ITEMS.registerItem("shield_obsidian", properties -> (Item) new BaubleShieldItem(EquippedItems.shield(properties).durability(1344).rarity(Rarity.RARE).fireResistant(), Items.OBSIDIAN).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredItem<Item> SHIELD_ANKH = ITEMS.registerItem("shield_ankh", properties -> (Item) new BaubleShieldItem(EquippedItems.shield(properties).durability(1680).rarity(Rarity.EPIC).fireResistant(), Items.OBSIDIAN).immuneTo(MobEffects.BLINDNESS, MobEffects.NAUSEA, MobEffects.DARKNESS, MobEffects.HUNGER, MobEffects.MINING_FATIGUE, MobEffects.WEAKNESS, MobEffects.SLOWNESS, MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER).withAttribute(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, "Bountiful shield knockback resistance", 1.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredItem<Item> MAGIC_MIRROR = ITEMS.registerItem("magic_mirror", properties -> new MirrorItem(rare(properties), MirrorItem.Destination.SPAWN, true));
    public static final DeferredItem<Item> POTION_RECALL = ITEMS.registerItem("potion_recall", properties -> new MirrorItem(properties.rarity(Rarity.UNCOMMON), MirrorItem.Destination.SPAWN, false));
    public static final DeferredItem<Item> WORMHOLE_MIRROR = ITEMS.registerItem("wormhole_mirror", properties -> new MirrorItem(epic(properties), MirrorItem.Destination.SPAWN_OR_OTHER_WHEN_CROUCHING, true));
    public static final DeferredItem<Item> POTION_WORMHOLE = ITEMS.registerItem("potion_wormhole", properties -> new MirrorItem(properties.rarity(Rarity.UNCOMMON), MirrorItem.Destination.OTHER_PLAYER, false));

    public static final DeferredItem<Item> BALLOON = bauble("balloon", properties -> new BaubleItem(rare(properties)));
    public static final DeferredItem<Item> LUCKY_HORSESHOE = bauble("lucky_horseshoe", properties -> new BaubleItem(rare(properties)));
    public static final DeferredItem<Item> HORSESHOE_BALLOON = bauble("horseshoe_balloon", properties -> new BaubleItem(epic(properties)));

    public static final DeferredItem<Item> AMULET_SIN_EMPTY = bauble("amulet_sin_empty", properties -> new BaubleItem(properties));
    public static final DeferredItem<Item> AMULET_SIN_GLUTTONY = bauble("amulet_sin_gluttony", properties -> new BaubleItem(epic(properties)));
    public static final DeferredItem<Item> AMULET_SIN_PRIDE = bauble("amulet_sin_pride", properties -> (Item) new BaubleItem(epic(properties)).withAttribute(Attributes.STEP_HEIGHT, uuid("6886f79c-5fb4-4f1c-b6bf-0ab66163e864"), "Pride Pendant step height", 0.64993D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredItem<Item> CROWN_GOLD = ITEMS.registerItem("crown_gold", properties -> new CrownItem(properties.rarity(Rarity.RARE)));
    public static final DeferredItem<Item> AMULET_SIN_WRATH = bauble("amulet_sin_wrath", properties -> (Item) new BaubleItem(epic(properties)).withAttribute(Attributes.ATTACK_DAMAGE, uuid("2d75d7e2-38bb-465e-a2b1-8a59c552fe40"), "Wrath Pendant damage", 2.0D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredItem<Item> BROKEN_HEART = bauble("broken_heart", properties -> new BaubleItem(epic(properties)));
    public static final DeferredItem<Item> AMULET_CROSS = bauble("amulet_cross", properties -> new BaubleItem(rare(properties)));

    public static final DeferredItem<Item> PHANTOM_PRISM = ITEMS.registerItem("phantom_prism", properties -> new PhantomPrismItem(epic(properties)));
    public static final DeferredItem<Item> DISINTEGRATION_TABLET = ITEMS.registerItem("disintegration_tablet", properties -> new ReusableCraftingItem(rare(properties)));
    public static final DeferredItem<Item> SPECTRAL_SILT = ITEMS.registerItem("spectral_silt", properties -> new GlintItem(properties));
    public static final DeferredItem<Item> RESPLENDENT_TOKEN = plain("resplendent_token");
    public static final DeferredItem<Item> FLARE_RED = ITEMS.registerItem("flare_red", properties -> new Item(properties));
    public static final DeferredItem<Item> FLARE_GUN = ITEMS.registerItem("flare_gun", properties -> new FlareGunItem(rare(properties)));

    private static DeferredItem<Item> bauble(String id, Function<Item.Properties, ? extends Item> supplier) {
        return ITEMS.registerItem(id, supplier);
    }

    private static DeferredItem<Item> plain(String id) {
        return ITEMS.registerItem(id, properties -> new BCItem(properties));
    }

    private static Item.Properties rare(Item.Properties properties) {
        return properties.rarity(Rarity.RARE);
    }

    private static Item.Properties epic(Item.Properties properties) {
        return properties.rarity(Rarity.EPIC);
    }

    private static Identifier uuid(String id) {
        return BountifulCurios.id("attribute/" + id);
    }

    private ModItems() {
    }
}
