package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public final class CrownModel extends HumanoidModel<LivingEntity> {
    private CrownModel(ModelPart root) {
        super(root);
        setAllVisible(false);
        head.visible = true;
    }

    public static CrownModel create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -29.0F, -8.0F, 16.0F, 16.0F, 16.0F,
                                new CubeDeformation(0.5F)),
                PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        return new CrownModel(LayerDefinition.create(mesh, 64, 32).bakeRoot());
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light,
                               int overlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        super.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
