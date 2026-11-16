package keletu.bountifulcurios.item;

import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

public final class CrownItem extends Item {
    public CrownItem(Properties properties) {
        super(properties.humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET)
                .repairable(Items.GOLD_INGOT)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                        .setEquipSound(ArmorMaterials.GOLD.equipSound())
                        .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, BountifulCurios.id("crown_gold"))).build()));
    }
}
