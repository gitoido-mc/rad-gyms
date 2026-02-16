package lol.gito.radgyms.common.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.COMMANDS_PREFIX
import lol.gito.radgyms.common.RadGyms.loadConfig
import lol.gito.radgyms.common.api.command.CommandInterface
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal

object ReloadCommand : CommandInterface {
    private const val NAME = "reload"

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literal(COMMANDS_PREFIX).then(
            literal(NAME)
                .requires { it.hasPermission(Commands.LEVEL_ADMINS) }
                .executes(::execute)
        )

        dispatcher.register(command)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        loadConfig()
        return Command.SINGLE_SUCCESS
    }
}
