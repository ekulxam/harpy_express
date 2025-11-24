package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static dev.doctor4t.trainmurdermystery.TMM.DOCTOR4T;

public class LockToSupportersCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:lockToSupporters")
                .requires(source -> source.getPlayer() != null && source.getPlayer().getUuid().equals(DOCTOR4T))
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> execute(context.getSource(), BoolArgumentType.getBool(context, "enabled"))))
        );
    }

    private static int execute(ServerCommandSource source, boolean value) {
        GameWorldComponent.KEY.get(source.getWorld()).setLockedToSupporters(value);
        return 1;
    }

}
