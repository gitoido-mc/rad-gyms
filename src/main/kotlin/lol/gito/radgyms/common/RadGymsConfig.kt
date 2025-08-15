/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common

import kotlinx.serialization.Serializable

@Serializable
data class RadGymsConfig(
    val debug: Boolean? = null,
    val maxEntranceUses: Int? = null,
    val shardRewards: Boolean? = null,
    val lapisBoostAmount: Int? = null,
    val ignoredSpecies: List<String>? = null,
    val ignoredForms: List<String>? = null,
) {
    fun combine(other: RadGymsConfig): RadGymsConfig {
        return this.copy(
            debug = other.debug ?: debug,
            maxEntranceUses = other.maxEntranceUses ?: maxEntranceUses,
            shardRewards = other.shardRewards ?: shardRewards,
            lapisBoostAmount = other.lapisBoostAmount ?: lapisBoostAmount,
            ignoredSpecies = other.ignoredSpecies ?: ignoredSpecies,
            ignoredForms = other.ignoredForms ?: ignoredForms
        )
    }
}