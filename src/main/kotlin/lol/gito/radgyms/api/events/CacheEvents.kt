/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import lol.gito.radgyms.api.events.cache.CacheRollPokeEvent

@Suppress("unused")
object CacheEvents {
    @JvmField
    val CACHE_ROLL_POKE = SimpleObservable<CacheRollPokeEvent>()
}