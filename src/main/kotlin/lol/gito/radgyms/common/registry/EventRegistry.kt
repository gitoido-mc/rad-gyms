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
import com.cobblemon.mod.common.api.reactive.SimpleObservable
import lol.gito.radgyms.api.events.ModEvents

@Suppress("unused")
object EventRegistry {
    @JvmField
    val CACHE_ROLL_POKE = SimpleObservable<ModEvents.CacheRollPokeEvent>()

    @JvmField
    val GYM_ENTER = SimpleObservable<ModEvents.GymEnterEvent>()

    @JvmField
    val GYM_LEAVE = SimpleObservable<ModEvents.GymLeaveEvent>()

    @JvmField
    val GENERATE_REWARD = SimpleObservable<ModEvents.GenerateRewardEvent>()

    @JvmField
    val GENERATE_TEAM = EventObservable<ModEvents.GenerateTeamEvent>()

    @JvmField
    val TRAINER_INTERACT = CancelableObservable<ModEvents.TrainerInteractEvent>()

    @JvmField
    val TRAINER_BATTLE_START = SimpleObservable<ModEvents.TrainerBattleStartEvent>()

    @JvmField
    val TRAINER_BATTLE_END = SimpleObservable<ModEvents.TrainerBattleEndEvent>()
}