package keletu.bountifulcurios;

import com.mojang.logging.LogUtils;
import keletu.bountifulcurios.compat.BrokenHeartFirstAidCompat;
import keletu.bountifulcurios.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BountifulCurios.MODID)
public final class BountifulCurios {
    public static final String MODID = "bountifulcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BountifulCurios() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ITEMS.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModLootModifiers.SERIALIZERS.register(modBus);
        ModTabs.TABS.register(modBus);
        if (ModList.get().isLoaded("firstaid")) {
            MinecraftForge.EVENT_BUS.register(BrokenHeartFirstAidCompat.class);
        }
    }
}
