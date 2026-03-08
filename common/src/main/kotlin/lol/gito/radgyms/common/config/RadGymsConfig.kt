/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import lol.gito.radgyms.common.api.serialization.KResourceLocationSerializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Rarity

@Serializable
data class RadGymsConfig(
    @SerialName("__version")
    val version: String = "0.4.5",
    val debug: Boolean? = null,
    val deriveAverageGymLevel: Boolean? = null,
    val minLevel: Int? = null,
    val maxLevel: Int? = null,
    val maxEntranceUses: Int? = null,
    val shardRewards: Boolean? = null,
    val ignoredSpecies: List<String>? = null,
    val pokeCachePools: Map<String, Set<String>>? = null,
    val pokeCacheBoosters: Map<
        @Serializable(KResourceLocationSerializer::class)
        ResourceLocation,
        Int,
        >? = null,
) {
    companion object {
        @Transient
        @JvmField
        val DEFAULT = RadGymsConfig(
            debug = false,
            // Should average team level be derived automatically
            deriveAverageGymLevel = true,
            // Gym level bounds
            minLevel = 10,
            maxLevel = 100,
            // Gym entrance max uses per player
            maxEntranceUses = 3,
            // Add shard rewards
            shardRewards = true,
            // Ignored species
            ignoredSpecies = listOf(
                "mega",
                "mega-x",
                "mega-y",
                "gmax",
            ),
            pokeCachePools = mutableMapOf(
                Rarity.COMMON.serializedName to mutableSetOf(
                    Rarity.COMMON.serializedName,
                ),
                Rarity.UNCOMMON.serializedName to mutableSetOf(
                    Rarity.UNCOMMON.serializedName,
                    Rarity.COMMON.serializedName,
                ),
                Rarity.RARE.serializedName to mutableSetOf(
                    Rarity.RARE.serializedName,
                    Rarity.UNCOMMON.serializedName,
                ),
                Rarity.EPIC.serializedName to mutableSetOf(
                    Rarity.EPIC.serializedName,
                    Rarity.RARE.serializedName,
                ),
            ),
            pokeCacheBoosters = mutableMapOf(
                ResourceLocation.parse("minecraft:lapis_lazuli") to 1,
                ResourceLocation.parse("minecraft:lapis_lazuli_block") to 9,
            ),
        )
    }

    fun combine(other: RadGymsConfig): RadGymsConfig = this.copy(
        debug = other.debug ?: debug,
        maxEntranceUses = other.maxEntranceUses ?: maxEntranceUses,
        shardRewards = other.shardRewards ?: shardRewards,
        ignoredSpecies = other.ignoredSpecies ?: ignoredSpecies,
        minLevel = other.minLevel ?: minLevel,
        maxLevel = other.maxLevel ?: maxLevel,
        deriveAverageGymLevel = other.deriveAverageGymLevel ?: deriveAverageGymLevel,
        pokeCachePools = other.pokeCachePools ?: pokeCachePools,
        pokeCacheBoosters = other.pokeCacheBoosters ?: pokeCacheBoosters,
    )
}
