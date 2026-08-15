package keletu.bountifulcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class SunglassesCurioRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "bountifulcurios", "textures/models/armor/sunglasses_layer_1.png");
    private final ModelPart head;

    public SunglassesCurioRenderer() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F,
                                new CubeDeformation(1.5F)),
                PartPose.ZERO);
        head = LayerDefinition.create(mesh, 64, 32).bakeRoot().getChild("head");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int light,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch) {
        if (slotContext.entity().isInvisible()) {
            return;
        }
        ICurioRenderer.followHeadRotations(slotContext.entity(), head);
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        head.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
