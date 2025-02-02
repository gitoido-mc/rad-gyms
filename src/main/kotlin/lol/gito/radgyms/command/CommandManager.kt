package lol.gito.radgyms.command

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

object CommandManager {
    fun forceLeave(context: CommandContext<ServerCommandSource>): Int {
        val serverPlayer = EntityArgumentType.getPlayer(context, "player")
        val world = serverPlayer.world

        return if (world is ServerWorld) {
            1;
        } else {
            serverPlayer.sendMessage(Text.translatable("cobgyms.lang.message.no_response"))
            -1;
        }
    }

    fun register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, access, environment ->
            dispatcher.register(
                CommandManager.literal("radgyms")
                .then(CommandManager.literal("forceLeave").requires { s -> s.hasPermissionLevel(2) }.then(
                        CommandManager.argument("player", EntityArgumentType.player()).executes(::forceLeave)
                    )))
        })
    }
}