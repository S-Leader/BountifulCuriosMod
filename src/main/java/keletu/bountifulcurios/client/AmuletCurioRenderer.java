package keletu.bountifulcurios.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class AmuletCurioRenderer implements ICurioRenderer.HumanoidRender {
    private final HumanoidModel<HumanoidRenderState> model;
    private final Identifier texture;

    public AmuletCurioRenderer(Identifier texture) {
        this.texture = texture;
        model = new HumanoidModel<>(LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.15F), 0), 64, 32).bakeRoot());
        model.head.visible = false;
        model.hat.visible = false;
        model.body.visible = true;
        model.rightArm.visible = false;
        model.leftArm.visible = false;
        model.rightLeg.visible = false;
        model.leftLeg.visible = false;
    }

    @Override
    public HumanoidModel<HumanoidRenderState> getModel(ItemStack stack, SlotContext context) { return model; }

    @Override
    public Identifier getModelTexture(ItemStack stack, SlotContext context) { return texture; }
}
