/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.api.reactive.EventObservable
import lol.gito.radgyms.api.event.ModEvents

@Suppress("unused")
object EventRegistry {
    @JvmField
    val CACHE_ROLL_POKE = EventObservable<ModEvents.CacheRollPokeEvent>()

    @JvmField
    val GYM_ENTER = EventObservable<ModEvents.GymEnterEvent>()

    @JvmField
    val GYM_LEAVE = EventObservable<ModEvents.GymLeaveEvent>()

    @JvmField
    val GENERATE_REWARD = EventObservable<ModEvents.GenerateRewardEvent>()

    @JvmField
    val GENERATE_TEAM = EventObservable<ModEvents.GenerateTeamEvent>()

    @JvmField
    val TRAINER_INTERACT = CancelableObservable<ModEvents.TrainerInteractEvent>()

    @JvmField
    val TRAINER_BATTLE_START = CancelableObservable<ModEvents.TrainerBattleStartEvent>()

    @JvmField
    val TRAINER_BATTLE_END = EventObservable<ModEvents.TrainerBattleEndEvent>()

    @JvmField
    val ENTER_SCREEN_OPEN = EventObservable<ModEvents.GymEnterScreenOpenEvent>()

    @JvmField
    val ENTER_SCREEN_CLOSE = EventObservable<ModEvents.GymEnterScreenCloseEvent>()

    @JvmField
    val LEAVE_SCREEN_OPEN = EventObservable<ModEvents.GymLeaveScreenOpenEvent>()

    @JvmField
    val LEAVE_SCREEN_CLOSE = EventObservable<ModEvents.GymLeaveScreenCloseEvent>()
}