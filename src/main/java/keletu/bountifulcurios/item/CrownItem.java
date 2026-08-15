package keletu.bountifulcurios.item;

import keletu.bountifulcurios.client.CrownModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CrownItem extends ArmorItem {
    public CrownItem(Properties properties) {
        super(ArmorMaterials.GOLD, Type.HELMET, properties);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "bountifulcurios:textures/models/armor/crowngold_layer_1.png";
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.GOLD_INGOT) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final HumanoidModel<LivingEntity> crownModel = CrownModel.create();

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                return slot == EquipmentSlot.HEAD ? crownModel : original;
            }
        });
    }
}
