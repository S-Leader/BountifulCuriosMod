package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.entity.FlareEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public final class FlareRenderer extends EntityRenderer<FlareEntity, FlareRenderState> {
    private static final Identifier TEXTURE = BountifulCurios.id("textures/entity/flare.png");
    private final FlareModel model;

    public FlareRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new FlareModel(context.bakeLayer(FlareModel.LAYER_LOCATION));
    }

    @Override
    public FlareRenderState createRenderState() { return new FlareRenderState(); }

    @Override
    public void extractRenderState(FlareEntity entity, FlareRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yaw = -entity.getYRot(partialTick);
        state.pitch = entity.getXRot(partialTick);
    }

    @Override
    public void submit(FlareRenderState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        poses.pushPose();
        poses.scale(-1, -1, 1);
        collector.order(0).submitModel(model, state, poses, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poses.popPose();
        super.submit(state, poses, collector, camera);
    }

    @Override
    protected int getBlockLightLevel(FlareEntity entity, BlockPos pos) { return 15; }
}
