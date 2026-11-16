package keletu.bountifulcurios.client;

import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public final class FlareModel extends EntityModel<FlareRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BountifulCurios.id("flare"), "main");
    private final ModelPart flare;

    public FlareModel(ModelPart root) {
        super(root);
        flare = root.getChild("flare");
    }

    public static LayerDefinition createBodyLayer() {
        var mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("flare", CubeListBuilder.create().texOffs(0, 0).addBox(-1, -1, -4, 2, 2, 8), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 16);
    }

    @Override
    public void setupAnim(FlareRenderState state) {
        super.setupAnim(state);
        flare.yRot = state.yaw * ((float) Math.PI / 180);
        flare.xRot = state.pitch * ((float) Math.PI / 180);
    }
}
