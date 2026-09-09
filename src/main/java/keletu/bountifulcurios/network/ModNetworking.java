package keletu.bountifulcurios.network;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.client.ClientWormholeScreenOpener;
import keletu.bountifulcurios.wormhole.WormholeTeleportManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public final class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BountifulCurios.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static final WormholeScreenOpener SCREEN_OPENER = DistExecutor.safeRunForDist(
            () -> ClientWormholeScreenOpener::new,
            () -> DedicatedServerWormholeScreenOpener::new);
    private static int packetId;

    public static void register() {
        CHANNEL.messageBuilder(OpenWormholeScreenPacket.class, packetId++,
                        NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OpenWormholeScreenPacket::encode)
                .decoder(OpenWormholeScreenPacket::decode)
                .consumerMainThread(OpenWormholeScreenPacket::handle)
                .add();
        CHANNEL.messageBuilder(SelectWormholeTargetPacket.class, packetId++,
                        NetworkDirection.PLAY_TO_SERVER)
                .encoder(SelectWormholeTargetPacket::encode)
                .decoder(SelectWormholeTargetPacket::decode)
                .consumerMainThread(SelectWormholeTargetPacket::handle)
                .add();
    }

    public static void openWormholeScreen(net.minecraft.server.level.ServerPlayer player,
                                          List<WormholeTarget> targets) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new OpenWormholeScreenPacket(targets));
    }

    public static void selectWormholeTarget(UUID target) {
        CHANNEL.sendToServer(new SelectWormholeTargetPacket(target));
    }

    public record WormholeTarget(UUID id, String name) {
    }

    private record OpenWormholeScreenPacket(List<WormholeTarget> targets) {
        private static void encode(OpenWormholeScreenPacket packet, FriendlyByteBuf buffer) {
            buffer.writeVarInt(packet.targets.size());
            for (WormholeTarget target : packet.targets) {
                buffer.writeUUID(target.id);
                buffer.writeUtf(target.name, 16);
            }
        }

        private static OpenWormholeScreenPacket decode(FriendlyByteBuf buffer) {
            int size = Math.min(buffer.readVarInt(), 1024);
            List<WormholeTarget> targets = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                targets.add(new WormholeTarget(buffer.readUUID(), buffer.readUtf(16)));
            }
            return new OpenWormholeScreenPacket(List.copyOf(targets));
        }

        private static void handle(OpenWormholeScreenPacket packet,
                                   Supplier<NetworkEvent.Context> context) {
            SCREEN_OPENER.open(packet.targets);
        }
    }

    private record SelectWormholeTargetPacket(UUID target) {
        private static void encode(SelectWormholeTargetPacket packet, FriendlyByteBuf buffer) {
            buffer.writeUUID(packet.target);
        }

        private static SelectWormholeTargetPacket decode(FriendlyByteBuf buffer) {
            return new SelectWormholeTargetPacket(buffer.readUUID());
        }

        private static void handle(SelectWormholeTargetPacket packet,
                                   Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getSender() != null) {
                WormholeTeleportManager.requestTeleport(context.getSender(), packet.target);
            }
        }
    }

    private ModNetworking() {
    }
}
