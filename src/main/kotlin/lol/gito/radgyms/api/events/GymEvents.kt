/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events

import com.cobblemon.mod.common.api.reactive.EventObservable
import com.cobblemon.mod.common.api.reactive.SimpleObservable
import lol.gito.radgyms.api.events.gym.*

@Suppress("unused")
object GymEvents {
    @JvmField
    val GYM_ENTER = SimpleObservable<GymEnterEvent>()

    @JvmField
    val GYM_LEAVE = SimpleObservable<GymLeaveEvent>()

    @JvmField
    val TRAINER_INTERACT = SimpleObservable<TrainerInteractEvent>()

    @JvmField
    val TRAINER_BATTLE_START = SimpleObservable<TrainerBattleStartEvent>()

    @JvmField
    val TRAINER_BATTLE_END = SimpleObservable<TrainerBattleEndEvent>()

    @JvmField
    val GENERATE_REWARD = SimpleObservable<GenerateRewardEvent>()

    @JvmField
    val GENERATE_TEAM = EventObservable<GenerateTeamEvent>()
}