package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.entity.FlareEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FlareRenderer extends EntityRenderer<FlareEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            BountifulCurios.MODID, "textures/entity/flare.png");

    private final FlareModel model;

    public FlareRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlareModel(context.bakeLayer(FlareModel.LAYER_LOCATION));
    }

    @Override
    public void render(FlareEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        float yaw = -Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTick, yaw, pitch);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    protected int getBlockLightLevel(FlareEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(FlareEntity entity) {
        return TEXTURE;
    }
}
