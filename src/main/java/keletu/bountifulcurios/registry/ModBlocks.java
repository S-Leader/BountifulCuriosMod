package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.block.WaterCandleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BountifulCurios.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BountifulCurios.MODID);

    public static final RegistryObject<Block> WATER_CANDLE = BLOCKS.register("water_candle",
            () -> new WaterCandleBlock(BlockBehaviour.Properties.copy(Blocks.CANDLE)
                    .lightLevel(state -> 7).noOcclusion()));

    public static final RegistryObject<Item> WATER_CANDLE_ITEM = BLOCK_ITEMS.register("water_candle",
            () -> new BlockItem(WATER_CANDLE.get(), new Item.Properties()));

    private ModBlocks() {
    }
}
