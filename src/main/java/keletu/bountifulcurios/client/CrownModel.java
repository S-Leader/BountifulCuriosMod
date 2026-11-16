package keletu.bountifulcurios.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public final class CrownModel extends HumanoidModel<HumanoidRenderState> {
    private CrownModel(ModelPart root) {
        super(root);
        head.visible = true;
        hat.visible = false;
        body.visible = false;
        rightArm.visible = false;
        leftArm.visible = false;
        rightLeg.visible = false;
        leftLeg.visible = false;
    }

    public static CrownModel create() {
        var mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        var root = mesh.getRoot().clearRecursively();
        var head = root.getChild("head");
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 0).addBox(-8, -29, -8, 16, 16, 16, new CubeDeformation(0.5F)), PartPose.ZERO.withScale(0.5F));
        return new CrownModel(LayerDefinition.create(mesh, 64, 32).bakeRoot());
    }
}
