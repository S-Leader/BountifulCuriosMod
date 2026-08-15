package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class BodyItemCurioRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int light,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch) {
        LivingEntity entity = slotContext.entity();
        if (entity.isInvisible()) {
            return;
        }

        poseStack.pushPose();
        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoid) {
            humanoid.body.translateAndRotate(poseStack);
        } else {
            ICurioRenderer.translateIfSneaking(poseStack, entity);
            ICurioRenderer.rotateIfSneaking(poseStack, entity);
        }

        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(0.5F, -0.25F, 0.72F);

        Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack,
                ItemDisplayContext.NONE,
                false, poseStack, buffer, entity.level(), light, OverlayTexture.NO_OVERLAY,
                entity.getId() + slotContext.index());
        poseStack.popPose();
    }
}
