package keletu.bountifulcurios.client;

import java.util.UUID;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.network.ModNetworking;
import keletu.bountifulcurios.registry.ModEntities;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class ClientEvents {
    public static void register(IEventBus bus) {
        bus.addListener(ClientEvents::registerEntityRenderers);
        bus.addListener(ClientEvents::registerLayerDefinitions);
        bus.addListener(ClientEvents::onClientSetup);
        bus.addListener(ClientEvents::registerExtensions);
        bus.addListener(ClientEvents::registerPayloads);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FLARE.get(), FlareRenderer::new);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FlareModel.LAYER_LOCATION, FlareModel::createBodyLayer);
    }

    private static void registerPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(ModNetworking.OpenWormholeScreenPacket.TYPE, (packet, context) -> context.enqueueWork(() -> WormholeSelectionScreen.open(packet.targets())));
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private final CrownModel crown = CrownModel.create();
            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
                return crown;
            }
        }, ModItems.CROWN_GOLD.get());
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ICurioRenderer.register(ModItems.SUNGLASSES.get(), SunglassesCurioRenderer::new);
            registerAmulet(ModItems.AMULET_CROSS.get(), "amulet_cross");
            registerAmulet(ModItems.AMULET_SIN_EMPTY.get(), "amulet_sin_empty");
            registerAmulet(ModItems.AMULET_SIN_GLUTTONY.get(), "amulet_sin_gluttony");
            registerAmulet(ModItems.AMULET_SIN_PRIDE.get(), "amulet_sin_pride");
            registerAmulet(ModItems.AMULET_SIN_WRATH.get(), "amulet_sin_wrath");
            ICurioRenderer.register(ModItems.SHIELD_COBALT.get(), BodyItemCurioRenderer::new);
            ICurioRenderer.register(ModItems.SHIELD_OBSIDIAN.get(), BodyItemCurioRenderer::new);
            ICurioRenderer.register(ModItems.SHIELD_ANKH.get(), BodyItemCurioRenderer::new);
        });
    }

    private static void registerAmulet(Item item, String name) {
        ICurioRenderer.register(item, () -> new AmuletCurioRenderer(BountifulCurios.id("textures/equipped/" + name + ".png")));
    }

    public static void selectTarget(UUID target) {
        ClientPacketDistributor.sendToServer(new ModNetworking.SelectWormholeTargetPacket(target));
    }

    private ClientEvents() {
    }
}
