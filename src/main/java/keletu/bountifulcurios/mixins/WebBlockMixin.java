package keletu.bountifulcurios.mixins;

import keletu.bountifulcurios.compat.CurioUtil;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public abstract class WebBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void bountifulcurios$freeActionIgnoresWeb(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo callback) {
        if (entity instanceof Player player
                && (CurioUtil.isEquipped(player, ModItems.RING_FREE_ACTION.get())
                || CurioUtil.isEquipped(player, ModItems.ANKH_CHARM.get())
                || CurioUtil.isEquipped(player, ModItems.SHIELD_ANKH.get()))) {
            callback.cancel();
        }
    }
}
