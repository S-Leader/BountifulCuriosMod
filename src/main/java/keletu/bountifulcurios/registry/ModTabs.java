package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BountifulCurios.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("bountifulcurios",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bountifulcurios"))
                    .icon(() -> new ItemStack(ModItems.OBSIDIAN_SKULL.get()))
                    .displayItems((parameters, output) -> {
                        ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        output.accept(ModBlocks.WATER_CANDLE_ITEM.get());
                    })
                    .build());

    private ModTabs() {
    }
}
