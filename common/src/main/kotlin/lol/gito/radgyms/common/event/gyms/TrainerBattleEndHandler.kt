/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.gym.GymTeleportScheduler
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsDimensions.GYM_DIMENSION
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.server.level.ServerPlayer

class TrainerBattleEndHandler(
    event: GymEvents.TrainerBattleEndEvent,
) {
    init {
        debug("Trainer battle end triggered")

        when (event.reason) {
            GymBattleEndReason.BATTLE_FLED, GymBattleEndReason.BATTLE_LOST -> handleGymLeave(event)
            GymBattleEndReason.BATTLE_WON -> handleGymWin(event)
        }
    }

    private fun handleGymWin(event: GymEvents.TrainerBattleEndEvent) {
        var defeatedLeader: Trainer? = null

        event.losers
            .filterIsInstance<TrainerEntityBattleActor>()
            .filter { it.entity is Trainer }
            .forEach { loser ->
                val trainer = loser.entity as Trainer
                trainer.defeated = true
                if (trainer.leader) defeatedLeader = trainer
                RadGyms.RCT.trainerRegistry.unregisterById(trainer.stringUUID)
            }

        if (defeatedLeader != null) {
            val winnerPlayers =
                event.winners
                    .filterIsInstance<PlayerBattleActor>()
                    .map { it.entity as ServerPlayer }

            val firstPlayer = winnerPlayers.first()
            val gym = RadGymsState.getGymForPlayer(firstPlayer)!!
            if (firstPlayer.level().dimension() == GYM_DIMENSION) {
                debug("Trying to spawn exit block")
                gym.let { GymTeardownService.spawnExitBlock(firstPlayer.server, it) }
            }

            winnerPlayers.forEach {
                GENERATE_REWARD.emit(GymEvents.GenerateRewardEvent(it, gym.template, gym.level, gym.type))
                it.displayClientMessage(tl("message.info.gym_complete"))
            }
        }
    }

    private fun handleGymLeave(event: GymEvents.TrainerBattleEndEvent) =
        event
            .battle
            .players
            .filter { it.level().dimension() == GYM_DIMENSION }
            .forEach {
                GymTeardownService
                    .withTeleportScheduler(GymTeleportScheduler())
                    .handleGymLeave(it)
            }
}
