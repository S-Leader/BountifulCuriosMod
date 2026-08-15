package keletu.bountifulcurios.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class AmuletCurioRenderer implements ICurioRenderer.HumanoidRender {
    private final HumanoidModel<LivingEntity> model;
    private final ResourceLocation texture;

    public AmuletCurioRenderer(ResourceLocation texture) {
        this.texture = texture;
        this.model = new HumanoidModel<>(LayerDefinition.create(
                        HumanoidModel.createMesh(new CubeDeformation(0.15F), 0.0F), 64, 32)
                .bakeRoot());
        model.setAllVisible(false);
        model.body.visible = true;
    }

    @Override
    public HumanoidModel<LivingEntity> getModel(ItemStack stack, SlotContext slotContext) {
        return model;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack stack, SlotContext slotContext) {
        return texture;
    }
}
