package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.entity.FlareEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class FlareModel extends EntityModel<FlareEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BountifulCurios.MODID, "flare"), "main");

    private final ModelPart flare;

    public FlareModel(ModelPart root) {
        this.flare = root.getChild("flare");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("flare",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 8.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 16);
    }

    @Override
    public void setupAnim(FlareEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        flare.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        flare.xRot = headPitch * ((float) Math.PI / 180.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                               int packedOverlay, float red, float green, float blue,
                               float alpha) {
        flare.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
