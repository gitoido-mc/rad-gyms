package lol.gito.radgyms.common.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

interface CommandInterface {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>)

    fun execute(context: CommandContext<CommandSourceStack>): Int
}
