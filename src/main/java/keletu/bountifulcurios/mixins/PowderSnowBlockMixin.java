package keletu.bountifulcurios.mixins;

import keletu.bountifulcurios.compat.CurioUtil;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void bountifulcurios$freeActionWalksOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(callback.getReturnValueZ() || (entity instanceof Player player
                && (CurioUtil.isEquipped(player, ModItems.RING_FREE_ACTION.get())
                || CurioUtil.isEquipped(player, ModItems.ANKH_CHARM.get())
                || CurioUtil.isEquipped(player, ModItems.SHIELD_ANKH.get()))));
    }
}
