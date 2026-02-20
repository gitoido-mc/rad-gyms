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
import com.mojang.brigadier.exceptions.CommandSyntaxException
import lol.gito.radgyms.common.COMMANDS_PREFIX
import lol.gito.radgyms.common.api.command.CommandInterface
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsDimensions.GYM_DIMENSION
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.selector.EntitySelector

object KickCommand : CommandInterface {
    private const val NAME = "kick"
    private const val PLAYER_ARG = "player"

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command =
            literal(COMMANDS_PREFIX).then(
                literal(NAME).requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                    argument(PLAYER_ARG, EntityArgument.player()).executes(::execute),
                ),
            )

        dispatcher.register(command)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val player =
            try {
                context.getArgument(PLAYER_ARG, EntitySelector::class.java).findSinglePlayer(context.source)
            } catch (_: CommandSyntaxException) {
                context.source.sendFailure(tl("message.error.command.kick.no_player", context.source.player!!.name))
                return -1
            }

        if (player.level().dimension() != GYM_DIMENSION) {
            if (context.source.isPlayer) {
                context.source.sendFailure(tl("message.error.command.kick.wrong_dim", context.source.player!!.name))
            }
            return -1
        }

        player.displayClientMessage(tl("message.info.command.op_kick"))

        RadGymsState.getGymForPlayer(player)?.let {
            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.KICK_COMMAND,
                    player = player,
                    gym = it,
                    completed = false,
                    usedRope = false,
                ),
            )
        }

        return Command.SINGLE_SUCCESS
    }
}
