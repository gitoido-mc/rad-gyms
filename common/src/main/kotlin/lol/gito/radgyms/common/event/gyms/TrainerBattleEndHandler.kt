/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.gym.GymTeleportScheduler
import lol.gito.radgyms.common.registry.RadGymsDimensions.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer

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
        val firstPlayer = winnerBattleActor.entity as ServerPlayer
        event.losers.forEach { loser ->
            if (loser.type == ActorType.NPC && loser is TrainerEntityBattleActor && loser.entity is Trainer) {
                (loser.entity as Trainer).defeated = true
            }
        }

        val defeatedLeader: Trainer? = event.losers
            .filter { it.type == ActorType.NPC }
            .filterIsInstance<TrainerEntityBattleActor>()
            .filter { it.entity is Trainer }
            .firstOrNull {
                val npc = it.entity as Trainer
                npc.defeated && npc.leader
            }
            ?.let {
                it.entity as Trainer
            }

        if (defeatedLeader != null) {
            val winnerPlayers = event.winners
                .filter { it.type == ActorType.PLAYER }
                .map { (it as PlayerBattleActor).entity as ServerPlayer }

            val gym = RadGymsState.getGymForPlayer(firstPlayer)!!

            if (firstPlayer.level().dimension() == RADGYMS_LEVEL_KEY) {
                gym.let { GymTeardownService.spawnExitBlock(firstPlayer.server, it) }
            }

            winnerPlayers.forEach {
                GENERATE_REWARD.emit(
                    GymEvents.GenerateRewardEvent(it, gym.template, gym.level, gym.type)
                )

                it.displayClientMessage(translatable(modId("message.info.gym_complete").toLanguageKey()))
            }
        }
    }

    private fun handleGymLeave(event: GymEvents.TrainerBattleEndEvent) = event
        .battle
        .players
        .filter { it.level().dimension() == RADGYMS_LEVEL_KEY }
        .forEach {
            GymTeardownService
                .withTeleportScheduler(GymTeleportScheduler())
                .handleGymLeave(it)
        }
}
