package keletu.bountifulcurios.wormhole;

import keletu.bountifulcurios.network.ModNetworking;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.*;

public final class WormholeTeleportManager {
    private static final int REQUEST_EXPIRY_TICKS = 20 * 20;
    private static final Map<UUID, List<TeleportRequest>> REQUESTS = new HashMap<>();

    public static boolean openSelection(ServerPlayer player) {
        List<ModNetworking.WormholeTarget> targets = availableTargets(player);
        if (targets.isEmpty()) {
            player.displayClientMessage(Component.translatable(
                    "item.bountifulcurios.potion_wormhole.nootherplayers"), true);
            return false;
        }
        ModNetworking.openWormholeScreen(player, targets);
        return true;
    }

    public static boolean hasTarget(ServerPlayer player) {
        return !availableTargets(player).isEmpty();
    }

    public static void requestTeleport(ServerPlayer origin, UUID targetId) {
        ServerPlayer target = origin.server.getPlayerList().getPlayer(targetId);
        if (target == null || !isValidTarget(origin, target)) {
            origin.displayClientMessage(Component.translatable(
                    "item.bountifulcurios.potion_wormhole.nootherplayers"), true);
            return;
        }

        int now = origin.server.getTickCount();
        cleanExpired(origin.server, now);
        List<TeleportRequest> requests = REQUESTS.computeIfAbsent(target.getUUID(),
                ignored -> new ArrayList<>());
        requests.removeIf(request -> request.origin().equals(origin.getUUID()));
        requests.add(new TeleportRequest(origin.getUUID(), now));

        origin.sendSystemMessage(Component.translatable(
                "message.bountifulcurios.wormhole.sent", target.getDisplayName()));

        Component accept = Component.translatable("message.bountifulcurios.wormhole.accept")
                .withStyle(style -> style.withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/wormhole accept " + origin.getGameProfile().getName())));
        Component reject = Component.translatable("message.bountifulcurios.wormhole.reject")
                .withStyle(style -> style.withColor(ChatFormatting.RED)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/wormhole deny " + origin.getGameProfile().getName())));
        target.sendSystemMessage(Component.translatable(
                        "message.bountifulcurios.wormhole.request", origin.getDisplayName())
                .append(" ").append(accept).append(" ").append(reject));
    }

    public static int respond(ServerPlayer target, boolean accept, @Nullable String originName) {
        MinecraftServer server = target.server;
        int now = server.getTickCount();
        cleanExpired(server, now);
        List<TeleportRequest> requests = REQUESTS.get(target.getUUID());
        if (requests == null || requests.isEmpty()) {
            target.sendSystemMessage(Component.translatable(
                    "message.bountifulcurios.wormhole.none"));
            return 0;
        }

        int handled = 0;
        for (int index = requests.size() - 1; index >= 0; index--) {
            TeleportRequest request = requests.get(index);
            ServerPlayer origin = server.getPlayerList().getPlayer(request.origin());
            if (originName != null && (origin == null
                    || !origin.getGameProfile().getName().equalsIgnoreCase(originName))) {
                continue;
            }

            requests.remove(index);
            handled++;
            if (origin == null) {
                continue;
            }
            if (!accept) {
                target.sendSystemMessage(Component.translatable(
                        "message.bountifulcurios.wormhole.denied_target",
                        origin.getDisplayName()));
                origin.sendSystemMessage(Component.translatable(
                        "message.bountifulcurios.wormhole.denied_origin",
                        target.getDisplayName()));
            } else {
                acceptRequest(origin, target);
            }
            if (originName != null) {
                break;
            }
        }

        if (requests.isEmpty()) {
            REQUESTS.remove(target.getUUID());
        }
        if (handled == 0) {
            target.sendSystemMessage(Component.translatable(
                    "message.bountifulcurios.wormhole.none_from",
                    originName == null ? "" : originName));
        }
        return handled;
    }

    private static void acceptRequest(ServerPlayer origin, ServerPlayer target) {
        if (origin.isSleeping()) {
            sendFailure(origin, target, "message.bountifulcurios.wormhole.sleeping");
            return;
        }
        if (origin.isPassenger()) {
            sendFailure(origin, target, "message.bountifulcurios.wormhole.riding");
            return;
        }
        if (!consumeWormholeItem(origin)) {
            sendFailure(origin, target, "message.bountifulcurios.wormhole.no_item");
            return;
        }

        target.sendSystemMessage(Component.translatable(
                "message.bountifulcurios.wormhole.accepted_target",
                origin.getDisplayName()));
        origin.sendSystemMessage(Component.translatable(
                "message.bountifulcurios.wormhole.accepted_origin",
                target.getDisplayName()));
        origin.stopRiding();
        origin.teleportTo(target.serverLevel(), target.getX(), target.getY(), target.getZ(),
                origin.getYRot(), origin.getXRot());
        origin.fallDistance = 0.0F;
        origin.level().playSound(null, origin.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static void sendFailure(ServerPlayer origin, ServerPlayer target, String key) {
        Component message = Component.translatable(key, origin.getDisplayName());
        origin.sendSystemMessage(message);
        target.sendSystemMessage(message);
    }

    private static boolean consumeWormholeItem(ServerPlayer player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(ModItems.WORMHOLE_MIRROR.get())) {
                return true;
            }
        }

        ItemStack potion = player.getMainHandItem().is(ModItems.POTION_WORMHOLE.get())
                ? player.getMainHandItem()
                : player.getOffhandItem().is(ModItems.POTION_WORMHOLE.get())
                ? player.getOffhandItem() : ItemStack.EMPTY;
        if (potion.isEmpty()) {
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack candidate = player.getInventory().getItem(slot);
                if (candidate.is(ModItems.POTION_WORMHOLE.get())) {
                    potion = candidate;
                    break;
                }
            }
        }
        if (potion.isEmpty()) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            potion.shrink(1);
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(bottle)) {
                player.drop(bottle, false);
            }
            player.containerMenu.broadcastChanges();
        }
        return true;
    }

    private static List<ModNetworking.WormholeTarget> availableTargets(ServerPlayer origin) {
        boolean survivalOnly = !origin.isCreative() && !origin.isSpectator();
        return origin.server.getPlayerList().getPlayers().stream()
                .filter(target -> target != origin)
                .filter(target -> !target.isSpectator())
                .filter(target -> !survivalOnly || !target.isCreative())
                .map(target -> new ModNetworking.WormholeTarget(target.getUUID(),
                        target.getGameProfile().getName()))
                .toList();
    }

    private static boolean isValidTarget(ServerPlayer origin, ServerPlayer target) {
        return target != origin && !target.isSpectator()
                && (origin.isCreative() || origin.isSpectator() || !target.isCreative());
    }

    private static void cleanExpired(MinecraftServer server, int now) {
        REQUESTS.entrySet().removeIf(entry -> {
            ServerPlayer target = server.getPlayerList().getPlayer(entry.getKey());
            entry.getValue().removeIf(request -> {
                if (now - request.createdTick() <= REQUEST_EXPIRY_TICKS) {
                    return false;
                }
                ServerPlayer origin = server.getPlayerList().getPlayer(request.origin());
                if (origin != null && target != null) {
                    origin.sendSystemMessage(Component.translatable(
                            "message.bountifulcurios.wormhole.expired_origin",
                            target.getDisplayName()));
                    target.sendSystemMessage(Component.translatable(
                            "message.bountifulcurios.wormhole.expired_target",
                            origin.getDisplayName()));
                }
                return true;
            });
            return entry.getValue().isEmpty();
        });
    }

    private record TeleportRequest(UUID origin, int createdTick) {
    }

    private WormholeTeleportManager() {
    }
}
