/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.api.event.ModEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.registry.DimensionRegistry.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.registry.EventRegistry.GENERATE_REWARD
import lol.gito.radgyms.state.RadGymsState
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable

class TrainerBattleEndHandler(@Suppress("unused") event: ModEvents.TrainerBattleEndEvent) {
    init {
        debug("Trainer battle end triggered")

        when (event.reason) {
            GymBattleEndReason.BATTLE_FLED, GymBattleEndReason.BATTLE_LOST -> handleGymLeave(event)
            GymBattleEndReason.BATTLE_WON -> handleGymWin(event)
        }
    }

    private fun handleGymWin(event: ModEvents.TrainerBattleEndEvent) {
        val winnerBattleActor = (event.winners.first { it.type == ActorType.PLAYER } as PlayerBattleActor)
        val player = winnerBattleActor.entity as ServerPlayerEntity
        event.losers.forEach { loser ->
            if (loser.type == ActorType.NPC && loser is TrainerEntityBattleActor && loser.entity is Trainer) {
                (loser.entity as Trainer).let { trainer ->
                    trainer.defeated = true
                    if (trainer.leader) {
                        val gym = RadGymsState.getGymForPlayer(player)!!
                        GymManager.spawnExitBlock(player)
                        GENERATE_REWARD.emit(
                            ModEvents.GenerateRewardEvent(
                                player,
                                gym.template,
                                gym.level,
                                gym.type
                            )
                        )
                        player.sendMessage(translatable(modId("message.info.gym_complete").toTranslationKey()))
                    }
                }
            }
        }
    }

    private fun handleGymLeave(event: ModEvents.TrainerBattleEndEvent) {
        event.battle.players
            .filter { it.world.registryKey == RADGYMS_LEVEL_KEY }
            .forEach { player ->
                GymManager.handleGymLeave(player)
            }
    }
}