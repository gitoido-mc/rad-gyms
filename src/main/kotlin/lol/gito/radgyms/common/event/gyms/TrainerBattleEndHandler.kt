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
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.registry.DimensionRegistry.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.state.RadGymsState
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable

class TrainerBattleEndHandler(event: GymEvents.TrainerBattleEndEvent) {
    init {
        debug("Trainer battle end triggered")

        when (event.reason) {
            GymBattleEndReason.BATTLE_FLED, GymBattleEndReason.BATTLE_LOST -> handleGymLeave(event)
            GymBattleEndReason.BATTLE_WON -> handleGymWin(event)
        }
    }

    private fun handleGymWin(event: GymEvents.TrainerBattleEndEvent) {
        val winnerBattleActor = (event.winners.first { it.type == ActorType.PLAYER } as PlayerBattleActor)
        val firstPlayer = winnerBattleActor.entity as ServerPlayerEntity
        event.losers.forEach { loser ->
            if (loser.type == ActorType.NPC && loser is TrainerEntityBattleActor && loser.entity is Trainer) {
                (loser.entity as Trainer).defeated = true
            }
        }

        val defeatedLeader: Trainer? = event.losers
            .filter { it.type == ActorType.NPC }
            .filter { it is TrainerEntityBattleActor }
            .filter { (it as TrainerEntityBattleActor).entity is Trainer }
            .firstOrNull {
                val npc = (it as TrainerEntityBattleActor).entity as Trainer
                npc.defeated && npc.leader
            }
            ?.let {
                (it as TrainerEntityBattleActor).entity as Trainer
            }

        if (defeatedLeader != null) {
            val winnerPlayers = event.winners
                .filter { it.type == ActorType.PLAYER }
                .map { (it as PlayerBattleActor).entity as ServerPlayerEntity }

            val gym = RadGymsState.getGymForPlayer(firstPlayer)!!

            gym.let { GymManager.spawnExitBlock(it) }

            winnerPlayers.forEach {
                GENERATE_REWARD.emit(
                    GymEvents.GenerateRewardEvent(it, gym.template, gym.level, gym.type)
                )

                it.sendMessage(translatable(modId("message.info.gym_complete").toTranslationKey()))
            }
        }
    }

    private fun handleGymLeave(event: GymEvents.TrainerBattleEndEvent) {
        event.battle.players.filter { it.world.registryKey == RADGYMS_LEVEL_KEY }.forEach { player ->
            GymManager.handleGymLeave(player)
        }
    }
}