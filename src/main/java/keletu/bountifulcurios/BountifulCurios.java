package keletu.bountifulcurios;

import com.mojang.logging.LogUtils;
import keletu.bountifulcurios.client.ClientEvents;
import keletu.bountifulcurios.item.FlywheelRingItem;
import keletu.bountifulcurios.network.ModNetworking;
import keletu.bountifulcurios.recipe.ModBrewingRecipes;
import keletu.bountifulcurios.registry.ModBlocks;
import keletu.bountifulcurios.registry.ModDataComponents;
import keletu.bountifulcurios.registry.ModEffects;
import keletu.bountifulcurios.registry.ModEntities;
import keletu.bountifulcurios.registry.ModItems;
import keletu.bountifulcurios.registry.ModLootModifiers;
import keletu.bountifulcurios.registry.ModTabs;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(BountifulCurios.MODID)
public final class BountifulCurios {
    public static final String MODID = "bountifulcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BountifulCurios(IEventBus bus, ModContainer container) {
        ModBlocks.BLOCKS.register(bus);
        ModBlocks.BLOCK_ITEMS.register(bus);
        ModEffects.EFFECTS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        ModLootModifiers.SERIALIZERS.register(bus);
        ModTabs.TABS.register(bus);
        ModDataComponents.COMPONENTS.register(bus);
        bus.addListener(ModNetworking::register);
        bus.addListener(FlywheelRingItem::registerEnergy);
        NeoForge.EVENT_BUS.addListener(ModBrewingRecipes::register);
        container.registerConfig(ModConfig.Type.COMMON, LootConfig.SPEC);
        if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) ClientEvents.register(bus);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
