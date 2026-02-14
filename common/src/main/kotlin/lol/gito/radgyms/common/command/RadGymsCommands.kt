package lol.gito.radgyms.common.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object RadGymsCommands {
    fun register(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        registry: CommandBuildContext,
        selection: Commands.CommandSelection
    ) {
    }
}
