package keletu.bountifulcurios.client;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = BountifulCurios.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {
    private static final ResourceLocation BLOCKING = new ResourceLocation("blocking");
    private static final ResourceLocation USING = new ResourceLocation("using");
    private static final ResourceLocation CHARGE = new ResourceLocation("charge");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerShieldProperties(ModItems.SHIELD_COBALT.get());
            registerShieldProperties(ModItems.SHIELD_OBSIDIAN.get());
            registerShieldProperties(ModItems.SHIELD_ANKH.get());
            registerFlyWheelProperties(ModItems.RING_FLYWHEEL.get());
            registerFlyWheelProperties(ModItems.RING_FLYWHEEL_ADVANCED.get());
            registerUsingProperty(ModItems.MAGIC_MIRROR.get());
            registerUsingProperty(ModItems.WORMHOLE_MIRROR.get());

            CuriosRendererRegistry.register(ModItems.SUNGLASSES.get(), SunglassesCurioRenderer::new);
            registerAmulet(ModItems.AMULET_CROSS.get(), "amulet_cross");
            registerAmulet(ModItems.AMULET_SIN_EMPTY.get(), "amulet_sin_empty");
            registerAmulet(ModItems.AMULET_SIN_GLUTTONY.get(), "amulet_sin_gluttony");
            registerAmulet(ModItems.AMULET_SIN_PRIDE.get(), "amulet_sin_pride");
            registerAmulet(ModItems.AMULET_SIN_WRATH.get(), "amulet_sin_wrath");

            CuriosRendererRegistry.register(ModItems.SHIELD_COBALT.get(), BodyItemCurioRenderer::new);
            CuriosRendererRegistry.register(ModItems.SHIELD_OBSIDIAN.get(), BodyItemCurioRenderer::new);
            CuriosRendererRegistry.register(ModItems.SHIELD_ANKH.get(), BodyItemCurioRenderer::new);
        });
    }

    private static void registerFlyWheelProperties(Item item) {
        ItemProperties.register(item, CHARGE, (stack, level, entity, seed) -> {
            IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY).orElseGet(null);
            return storage == null ? 0.0F : (float) storage.getEnergyStored() / (float) storage.getMaxEnergyStored();
        });
    }

    private static void registerShieldProperties(Item item) {
        ItemProperties.register(item, BLOCKING, (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    private static void registerUsingProperty(Item item) {
        ItemProperties.register(item, USING, (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    private static void registerAmulet(Item item, String texture) {
        ResourceLocation location = new ResourceLocation(BountifulCurios.MODID, "textures/equipped/" + texture + ".png");
        CuriosRendererRegistry.register(item, () -> new AmuletCurioRenderer(location));
    }

    private ClientEvents() {
    }
}
