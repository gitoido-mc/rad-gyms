/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.command

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.mojang.brigadier.context.CommandContext
import de.maxhenkel.admiral.MinecraftAdmiral
import de.maxhenkel.admiral.annotations.Command
import de.maxhenkel.admiral.annotations.MinMax
import de.maxhenkel.admiral.annotations.Name
import de.maxhenkel.admiral.annotations.RequiresPermissionLevel
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.loadConfig
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.gym.GymTemplate
import lol.gito.radgyms.common.pokecache.CacheHandler
import lol.gito.radgyms.common.registry.RadGymsDimensions.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.util.displayClientMessage
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component.literal
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Rarity

@Command("radgyms")
object CommandRegistry {
    @Suppress("unused")
    @Command("op:kick")
    @RequiresPermissionLevel(4)
    fun kick(
        context: CommandContext<CommandSourceStack>, @Name("player") player: ServerPlayer
    ): Int {
        if (player.level().dimension() != RADGYMS_LEVEL_KEY) {
            if (context.source.isPlayer) {
                context.source.sendFailure(
                    translatable(
                        modId("message.error.command.kick").toLanguageKey(), context.source.player!!.name
                    )
                )
            }
            return -1
        }

        player.displayClientMessage(
            translatable(modId("message.info.command.op_kick").toLanguageKey())
        )

        RadGymsState.getGymForPlayer(player)?.let {
            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.KICK_COMMAND,
                    player = player,
                    gym = it,
                    type = it.type,
                    level = it.level,
                    completed = false,
                    usedRope = false
                )
            )
        }

        return 1
    }

    @Suppress("unused")
    @Command("reload")
    @RequiresPermissionLevel(4)
    fun reloadConfig(
        context: CommandContext<CommandSourceStack>
    ): Int {
        loadConfig()
        return 1
    }

    @Suppress("unused")
    @Command("debug:reward")
    @RequiresPermissionLevel(4)
    fun debugReward(
        context: CommandContext<CommandSourceStack>,
        @Name("template") template: String,
        @Name("level") @MinMax(min = "1", max = "100") level: Int,
        @Name("type") type: String?
    ): Int {
        if (context.source.player != null) {
            val gymDto = RadGyms.gymTemplateRegistry.getTemplateOrDefault(template)

            if (gymDto == null) {
                context.source.sendFailure(
                    translatable(
                        modId("message.error.command.debug_reward.no_template").toLanguageKey(), template
                    )
                )
                return -1
            }

            level.apply {
                this.coerceIn(1..100)
            }

            val gymType = when (type) {
                null -> ElementalTypes.all().random().showdownId
                else -> type
            }

            GENERATE_REWARD.emit(
                GymEvents.GenerateRewardEvent(
                    context.source.playerOrException,
                    GymTemplate.fromDto(context.source.playerOrException, gymDto, level, type),
                    level,
                    gymType
                )
            )

            context.source.sendSystemMessage(
                translatable(
                    modId("message.info.command.debug_reward").toLanguageKey(), template, translatable(
                        cobblemonResource("type.${type}").toLanguageKey()
                    ), level
                )
            )
        } else {
            context.source.sendFailure(
                translatable(modId("message.error.command.debug_reward.no_player").toLanguageKey())
            )
            return -1
        }
        return 1
    }

    @Suppress("unused")
    @Command("debug:cache")
    @RequiresPermissionLevel(4)
    fun debugCache(
        context: CommandContext<CommandSourceStack>,
        @Name("type") type: String,
        @Name("rarity") rarity: String,
        @Name("shiny_boost") shinyBoost: Int?
    ): Int {
        if (context.source.player != null) {
            try {
                val rarityEnum = Rarity.valueOf(rarity.uppercase())
                val typeEnum = ElementalTypes.get(type) ?: throw RuntimeException("cannot get elemental type: $type")
                val poke: Pokemon = CacheHandler.getPoke(
                    typeEnum,
                    rarityEnum,
                    context.source.player!!,
                    shinyBoost
                )

                CACHE_ROLL_POKE.emit(
                    GymEvents.CacheRollPokeEvent(
                        context.source.player!!,
                        poke,
                        typeEnum.toString().lowercase(),
                        rarityEnum,
                        shinyBoost ?: 0
                    )
                )

            } catch (e: Exception) {
                context.source.player!!.sendSystemMessage(
                    literal("Cannot generate $rarity $type poke, caught error: ${e.message}")
                )
                return -1
            }
        } else {
            context.source.sendFailure(
                literal("Cannot generate reward for ${context.source.displayName}")
            )
            return -1
        }
        return 1
    }

    fun register() {
        debug("Registering chat commands")
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, registryAccess, _ ->
                MinecraftAdmiral.builder(dispatcher, registryAccess).addCommandClasses(this::class.java).build()
            })
    }
}
