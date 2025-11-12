/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.api.event.ModEvents
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.registry.DimensionRegistry

class TrainerBattleEndHandler(@Suppress("unused") event: ModEvents.TrainerBattleEndEvent) {
    init {
        debug("Trainer battle end triggered")

        when (event.reason) {
            GymBattleEndReason.BATTLE_FLED, GymBattleEndReason.BATTLE_LOST -> handleGymLeave(event)
            GymBattleEndReason.BATTLE_WON ->
        }
    }

    private fun handleGymLeave(event: ModEvents.TrainerBattleEndEvent) {
        event.battle.players
            .filter { it.world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY }
            .forEach { player ->
                GymManager.handleGymLeave(player)
            }
    }
}