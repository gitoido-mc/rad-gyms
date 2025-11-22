/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.entity.Trainer


@Suppress("FunctionName")
fun RadGymsTrainerCheckerPredicate(actor: BattleActor): Boolean {
    if (actor.type != ActorType.NPC) return false
    if (actor !is TrainerEntityBattleActor) return false
    if (actor.entity !is Trainer) return false
    return true
}


fun hasRadGymsTrainers(event: BattleFledEvent): Boolean = event.battle.actors
    .any(::RadGymsTrainerCheckerPredicate)

fun hasRadGymsTrainers(event: BattleFaintedEvent): Boolean = event.battle.actors
    .any(::RadGymsTrainerCheckerPredicate)

fun hasRadGymsTrainers(event: BattleVictoryEvent): Boolean = event.battle.actors
    .any(::RadGymsTrainerCheckerPredicate)

fun hasRadGymsTrainers(event: BattleStartedEvent.Pre): Boolean = event.battle.actors
    .any(::RadGymsTrainerCheckerPredicate)