package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class BodyItemCurioRenderer implements ICurioRenderer {
    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext context, PoseStack poses, SubmitNodeCollector collector, int light, S state, RenderLayerParent<S, M> parent, EntityRendererProvider.Context renderer, float yRot, float xRot) {
        if (state.isInvisible) return;
        ItemStackRenderState item = new ItemStackRenderState();
        renderer.getItemModelResolver().updateForLiving(item, stack, ItemDisplayContext.NONE, context.entity());
        poses.pushPose();
        if (parent.getModel() instanceof HumanoidModel<?> humanoid) humanoid.body.translateAndRotate(poses);
        poses.scale(0.6F, 0.6F, 0.6F);
        poses.mulPose(Axis.ZP.rotationDegrees(180));
        poses.translate(0.5F, -0.25F, 0.72F);
        item.submit(poses, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poses.popPose();
    }
}
