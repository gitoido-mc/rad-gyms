package lol.gito.radgyms.common.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.RadGyms.loadConfig
import lol.gito.radgyms.common.api.command.CommandInterface
import lol.gito.radgyms.common.helper.root
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ReloadCommand : CommandInterface {
    private const val NAME = "reload"

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = root(NAME)
            .requires { it.hasPermission(Commands.LEVEL_ADMINS) }
            .executes(::execute)

        dispatcher.register(command)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        loadConfig()
        return Command.SINGLE_SUCCESS
    }
}
