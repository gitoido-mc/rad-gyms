/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.command

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.player
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.COMMANDS_PREFIX
import lol.gito.radgyms.common.RadGyms.config
import lol.gito.radgyms.common.api.command.CommandInterface
import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.command.argument.ElementalTypeArgumentType
import lol.gito.radgyms.common.command.argument.GymTemplateArgumentType
import lol.gito.radgyms.common.gym.GymTemplate
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.helper.tlc
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

object GiveReward : CommandInterface {
    private const val NAME = "give"
    private const val SUB = "reward"
    private const val TEMPLATE = "template"
    private const val LEVEL = "level"
    private const val TYPE = "type"
    private const val PLAYER = "player"

    @Suppress("CheckedExceptionsKotlin")
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val selfCommand =
            literal(COMMANDS_PREFIX).then(
                literal(NAME).requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                    literal(SUB).then(
                        argument(TEMPLATE, GymTemplateArgumentType.templates()).then(
                            argument(LEVEL, IntegerArgumentType.integer(1, Cobblemon.config.maxPokemonLevel)).then(
                                argument(TYPE, ElementalTypeArgumentType.type()).executes {
                                    execute(
                                        it,
                                        it.player(),
                                        GymTemplateArgumentType.getTemplate(it, TEMPLATE),
                                        IntegerArgumentType.getInteger(it, LEVEL),
                                        ElementalTypeArgumentType.getType(it, TYPE),
                                    )
                                },
                            ),
                        ),
                    ),
                ),
            )

        val otherCommand =
            literal(COMMANDS_PREFIX).then(
                literal(NAME).requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                    literal(SUB).then(
                        argument(TEMPLATE, GymTemplateArgumentType.templates()).then(
                            argument(LEVEL, IntegerArgumentType.integer(1, Cobblemon.config.maxPokemonLevel)).then(
                                argument(TYPE, ElementalTypeArgumentType.type()).then(
                                    argument(PLAYER, EntityArgument.player()).executes {
                                        execute(
                                            it,
                                            EntityArgument.getPlayer(it, PLAYER),
                                            GymTemplateArgumentType.getTemplate(it, TEMPLATE),
                                            IntegerArgumentType.getInteger(it, LEVEL),
                                            ElementalTypeArgumentType.getType(it, TYPE),
                                        )
                                    },
                                ),
                            ),
                        ),
                    ),
                ),
            )

        dispatcher.register(selfCommand)
        dispatcher.register(otherCommand)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int = -1

    fun execute(
        context: CommandContext<CommandSourceStack>,
        player: ServerPlayer,
        template: GymJson,
        level: Int,
        type: ElementalType,
    ): Int {
        GENERATE_REWARD.emit(
            GymEvents.GenerateRewardEvent(
                player,
                GymTemplate.fromDto(
                    player,
                    template,
                    level.coerceIn(config.minLevel!!, Cobblemon.config.maxPokemonLevel),
                    type.showdownId,
                ),
                level,
                template.id,
            ),
        )

        context.source.sendSystemMessage(
            tl(
                key = "message.info.command.debug_reward",
                template.id,
                tlc("type.${type.showdownId}"),
                level,
            ),
        )

        return Command.SINGLE_SUCCESS
    }
}
