/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.config

import kotlinx.serialization.Serializable

@Serializable
data class RadGymsConfig(
    val debug: Boolean? = null,
    val maxEntranceUses: Int? = null,
    val shardRewards: Boolean? = null,
    val lapisBoostAmount: Int? = null,
    val ignoredSpecies: List<String>? = null,
    val ignoredForms: List<String>? = null,
    val deriveAverageGymLevel: Boolean? = null,
    val minLevel: Int? = null,
    val maxLevel: Int? = null,

    val cacheSettings: Map<String, CacheTierConfig>? = null
) {
    @Serializable
    data class CacheTierConfig(
        val level: Int,
        // Which pools to pull from? e.g. ["common", "uncommon"]
        val poolSources: List<String>,
        // Percentage chances for IVs (Must range 0-100)
        val ivRules: List<IvRule>
    )

    @Serializable
    data class IvRule(
        val chance: Double, // e.g., 5.0 for 5%
        val minPerfectIVs: Int // e.g., 6
    )

    fun combine(other: RadGymsConfig): RadGymsConfig {
        return this.copy(
            debug = other.debug ?: debug,
            maxEntranceUses = other.maxEntranceUses ?: maxEntranceUses,
            shardRewards = other.shardRewards ?: shardRewards,
            lapisBoostAmount = other.lapisBoostAmount ?: lapisBoostAmount,
            ignoredSpecies = other.ignoredSpecies ?: ignoredSpecies,
            ignoredForms = other.ignoredForms ?: ignoredForms,
            minLevel = other.minLevel ?: minLevel,
            maxLevel = other.maxLevel ?: maxLevel,
            deriveAverageGymLevel = other.deriveAverageGymLevel ?: deriveAverageGymLevel,
            cacheSettings = other.cacheSettings ?: cacheSettings
        )
    }
}