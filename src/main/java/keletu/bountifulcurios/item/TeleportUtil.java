package keletu.bountifulcurios.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.portal.TeleportTransition;

public final class TeleportUtil {
    public static boolean teleportToSpawn(ServerPlayer player) {
        player.stopRiding();
        TeleportTransition destination = player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
        if (player.teleport(destination) == null) return false;
        player.fallDistance = 0.0;
        player.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    private TeleportUtil() {
    }
}
