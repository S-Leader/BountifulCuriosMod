package keletu.bountifulcurios.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Comparator;

public final class TeleportUtil {
    public static boolean teleportToSpawn(ServerPlayer player) {
        ResourceKey<Level> dimension = player.getRespawnDimension();
        ServerLevel targetLevel = player.server.getLevel(dimension);
        if (targetLevel == null) {
            targetLevel = player.server.overworld();
        }

        BlockPos spawn = player.getRespawnPosition();
        if (spawn == null || !dimension.equals(targetLevel.dimension())) {
            spawn = targetLevel.getSharedSpawnPos();
        }

        player.stopRiding();
        player.teleportTo(targetLevel, spawn.getX() + 0.5D, spawn.getY() + 0.1D,
                spawn.getZ() + 0.5D, player.getYRot(), player.getXRot());
        finishTeleport(player);
        return true;
    }

    public static boolean teleportToAnotherPlayer(ServerPlayer player) {
        ServerPlayer target = player.server.getPlayerList().getPlayers().stream()
                .filter(candidate -> candidate != player)
                .min(Comparator.comparingDouble(player::distanceToSqr))
                .orElse(null);
        if (target == null) {
            player.displayClientMessage(ComponentKeys.NO_TARGET, true);
            return false;
        }
        player.stopRiding();
        player.teleportTo(target.serverLevel(), target.getX(), target.getY(), target.getZ(),
                player.getYRot(), player.getXRot());
        finishTeleport(player);
        return true;
    }

    private static void finishTeleport(ServerPlayer player) {
        player.fallDistance = 0.0F;
        player.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static final class ComponentKeys {
        private static final net.minecraft.network.chat.Component NO_TARGET =
                net.minecraft.network.chat.Component.translatable(
                        "item.bountifulcurios.potion_wormhole.nootherplayers");
    }

    private TeleportUtil() {
    }
}
