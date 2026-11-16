package keletu.bountifulcurios.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import keletu.bountifulcurios.loot.TrialRewardContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrialSpawner.class)
public abstract class TrialSpawnerMixin {
    @WrapMethod(method = "ejectReward")
    private void bountifulcurios$rewardContext(ServerLevel level, BlockPos pos, ResourceKey<LootTable> table, Operation<Void> original) {
        TrialRewardContext.duringReward((TrialSpawner) (Object) this, () -> original.call(level, pos, table));
    }
}
