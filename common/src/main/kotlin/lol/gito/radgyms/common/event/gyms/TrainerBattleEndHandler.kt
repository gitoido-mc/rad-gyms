/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.bedrockk.molang.runtime.value.DoubleValue
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsDimensions.GYM_DIMENSION
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.server.level.ServerPlayer

class TrainerBattleEndHandler(val event: GymEvents.TrainerBattleEndEvent) {
    init {
        debug("Trainer battle end triggered")

        when (event.reason) {
            GymBattleEndReason.BATTLE_FLED, GymBattleEndReason.BATTLE_LOST -> handleGymLeave()
            GymBattleEndReason.BATTLE_WON -> handleGymWin()
        }
    }

    private fun handleGymWin() {
        var defeatedLeader: NPCEntity? = null

        event.losers
            .filterIsInstance<TrainerEntityBattleActor>()
            .filter { it.entity is NPCEntity }
            .forEach {
                val t = it.entity as NPCEntity
                t.config.setDirectly("defeated", DoubleValue.ONE)
                if (t.config.map.contains("leader") && t.config.map["leader"] == DoubleValue.ONE) {
                    debug("Confirmed leader defeat")
                    defeatedLeader = t
                }
            }

        if (defeatedLeader != null) {
            val wp = event.winners
                .filterIsInstance<PlayerBattleActor>()
                .map { it.entity as ServerPlayer }

            val fp = wp.first()
            val g = RadGymsState.getGymForPlayer(fp)!!
            if (fp.level().dimension() == GYM_DIMENSION) {
                debug("Trying to spawn exit block")
                g.let { GymTeardownService.spawnExitBlock(fp.server, it) }
            }

            wp.forEach {
                GENERATE_REWARD.emit(GymEvents.GenerateRewardEvent(it, g.template, g.level, g.type))
                it.displayClientMessage(tl(modId("message.info.gym_complete")))
            }
        }
    }

    private fun handleGymLeave() = event
        .battle
        .players
        .filter { it.level().dimension() == GYM_DIMENSION }
        .forEach(GymTeardownService::handleGymLeave)
}
