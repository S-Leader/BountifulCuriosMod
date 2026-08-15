package keletu.bountifulcurios.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WaterCandleBlock extends Block {
    private static final int TICK_INTERVAL = 80;
    private static final double ACTIVE_RADIUS = 24.0D;

    public WaterCandleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
                        boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && !oldState.is(this)) {
            level.scheduleTick(pos, this, TICK_INTERVAL);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.hasNearbyAlivePlayer(pos.getX() + 0.5D, pos.getY() + 0.5D,
                pos.getZ() + 0.5D, ACTIVE_RADIUS)
                && level.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(ACTIVE_RADIUS))
                .size() < 20) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            int distance = 18 + random.nextInt(15);
            BlockPos spawnPos = pos.offset((int) Math.round(Math.cos(angle) * distance),
                    random.nextInt(9) - 4, (int) Math.round(Math.sin(angle) * distance));
            NaturalSpawner.spawnCategoryForPosition(MobCategory.MONSTER, level, spawnPos);
        }
        level.scheduleTick(pos, this, TICK_INTERVAL);
    }
}
