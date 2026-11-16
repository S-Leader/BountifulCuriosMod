package keletu.bountifulcurios.network;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.wormhole.WormholeTeleportManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class ModNetworking {
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(OpenWormholeScreenPacket.TYPE, OpenWormholeScreenPacket.CODEC);
        registrar.playToServer(SelectWormholeTargetPacket.TYPE, SelectWormholeTargetPacket.CODEC, (payload, context) -> context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) WormholeTeleportManager.requestTeleport(player, payload.target());
        }));
    }

    public static void openWormholeScreen(ServerPlayer player, List<WormholeTarget> targets) {
        PacketDistributor.sendToPlayer(player, new OpenWormholeScreenPacket(List.copyOf(targets)));
    }

    public record WormholeTarget(UUID id, String name) {
    }

    public record SelectWormholeTargetPacket(UUID target) implements CustomPacketPayload {
        public static final Type<SelectWormholeTargetPacket> TYPE = new Type<>(BountifulCurios.id("wormhole_target"));
        public static final StreamCodec<ByteBuf, SelectWormholeTargetPacket> CODEC = UUIDUtil.STREAM_CODEC.map(SelectWormholeTargetPacket::new, SelectWormholeTargetPacket::target);
        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record OpenWormholeScreenPacket(List<WormholeTarget> targets) implements CustomPacketPayload {
        public static final Type<OpenWormholeScreenPacket> TYPE = new Type<>(BountifulCurios.id("wormhole_screen"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenWormholeScreenPacket> CODEC = new StreamCodec<>() {
            @Override
            public OpenWormholeScreenPacket decode(RegistryFriendlyByteBuf buffer) {
                int size = buffer.readVarInt();
                if (size < 0 || size > 1024) throw new IllegalArgumentException("Invalid wormhole target count");
                List<WormholeTarget> targets = new ArrayList<>(size);
                for (int i = 0; i < size; i++) targets.add(new WormholeTarget(UUIDUtil.STREAM_CODEC.decode(buffer), buffer.readUtf(16)));
                return new OpenWormholeScreenPacket(List.copyOf(targets));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, OpenWormholeScreenPacket payload) {
                buffer.writeVarInt(payload.targets().size());
                for (WormholeTarget target : payload.targets()) {
                    UUIDUtil.STREAM_CODEC.encode(buffer, target.id());
                    buffer.writeUtf(target.name(), 16);
                }
            }
        };
        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    private ModNetworking() {
    }
}
