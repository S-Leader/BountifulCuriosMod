package keletu.bountifulcurios.client;

import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class SunglassesCurioRenderer implements ICurioRenderer.HumanoidRender {
    private final HumanoidModel<HumanoidRenderState> model;

    public SunglassesCurioRenderer() {
        var mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        var head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("glasses", CubeListBuilder.create().texOffs(0, 0).addBox(-8, -16, -8, 16, 16, 16, new CubeDeformation(1.5F)), PartPose.ZERO.withScale(0.5F));
        mesh.getRoot().addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        model = new HumanoidModel<>(LayerDefinition.create(mesh, 64, 32).bakeRoot());
        model.head.visible = true;
        model.hat.visible = false;
        model.body.visible = false;
        model.rightArm.visible = false;
        model.leftArm.visible = false;
        model.rightLeg.visible = false;
        model.leftLeg.visible = false;
    }

    @Override
    public HumanoidModel<HumanoidRenderState> getModel(ItemStack stack, SlotContext context) { return model; }

    @Override
    public Identifier getModelTexture(ItemStack stack, SlotContext context) {
        return BountifulCurios.id("textures/models/armor/sunglasses_layer_1.png");
    }
}
