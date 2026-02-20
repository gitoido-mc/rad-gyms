/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.COMMANDS_PREFIX
import lol.gito.radgyms.common.RadGyms.loadConfig
import lol.gito.radgyms.common.api.command.CommandInterface
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal

object ReloadCommand : CommandInterface {
    private const val NAME = "reload"

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command =
            literal(COMMANDS_PREFIX).then(
                literal(NAME)
                    .requires { it.hasPermission(Commands.LEVEL_ADMINS) }
                    .executes(::execute),
            )

        dispatcher.register(command)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        loadConfig()
        context.source.player?.displayClientMessage(tl("message.info.command.config_reloaded"))
        return Command.SINGLE_SUCCESS
    }
}
