package keletu.bountifulcurios.wormhole;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BountifulCurios.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WormholeCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("wormhole")
                .then(response("accept", true))
                .then(response("acc", true))
                .then(response("deny", false))
                .then(response("reject", false));
        event.getDispatcher().register(command);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> response(String name,
                                                                       boolean accept) {
        return Commands.literal(name)
                .executes(context -> respond(context.getSource(), accept, null))
                .then(Commands.argument("player", StringArgumentType.word())
                        .executes(context -> respond(context.getSource(), accept,
                                StringArgumentType.getString(context, "player"))));
    }

    private static int respond(CommandSourceStack source, boolean accept, String originName)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = source.getPlayerOrException();
        return WormholeTeleportManager.respond(target, accept, originName);
    }

    private WormholeCommands() {
    }
}
