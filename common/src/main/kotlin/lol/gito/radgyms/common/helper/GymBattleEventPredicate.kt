/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.helper

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.entity.Trainer

@Suppress("FunctionName")
fun TrainerCheckerPredicate(actor: BattleActor): Boolean {
    if (actor !is TrainerEntityBattleActor) return false
    if (actor.entity !is Trainer) return false
    return true
}

fun hasGymTrainers(event: BattleFledEvent): Boolean = event.battle.actors
    .any(::TrainerCheckerPredicate)

fun hasGymTrainers(event: BattleFaintedEvent): Boolean = event.battle.actors
    .any(::TrainerCheckerPredicate)

fun hasGymTrainers(event: BattleVictoryEvent): Boolean = event.battle.actors
    .any(::TrainerCheckerPredicate)

fun hasGymTrainers(event: BattleStartedEvent.Pre): Boolean = event.battle.actors
    .any(::TrainerCheckerPredicate)
