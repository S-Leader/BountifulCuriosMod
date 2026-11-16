package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.block.WaterCandleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BountifulCurios.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(BountifulCurios.MODID);
    public static final DeferredBlock<WaterCandleBlock> WATER_CANDLE = BLOCKS.registerBlock("water_candle", WaterCandleBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE).lightLevel(state -> 7).noOcclusion());
    public static final DeferredItem<BlockItem> WATER_CANDLE_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("water_candle", WATER_CANDLE);

    private ModBlocks() {
    }
}
