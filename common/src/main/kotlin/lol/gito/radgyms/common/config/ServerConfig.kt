/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
@file:Suppress("LongParameterList")

package lol.gito.radgyms.common.config

import net.minecraft.world.item.Rarity

@Suppress("MagicNumber")
class ServerConfig(
    // Debug logging
    val debug: Boolean = false,

    // Should average team level be derived automatically
    val deriveAverageGymLevel: Boolean = true,

    // Gym level bounds
    val minLevel: Int = 10,
    val maxLevel: Int = 100,

    // Gym entrance max uses per player
    val maxEntranceUses: Int = 3,

    // Cache shiny boost amount per unit of lapis
    // todo: refactor to support other items
    val lapisBoostAmount: Int = 1,

    // Add shard rewards
    val shardRewards: Boolean = true,

    // Ignored species
    val ignoredSpecies: List<String> = listOf(
        "mega",
        "mega-x",
        "mega-y",
        "gmax",
    ),

    // Caches
    val pokeCachePools: MutableMap<String, Set<String>> = mutableMapOf(
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
) {
    companion object {
        fun create(
            maxEntranceUses: Int,
            shardRewards: Boolean,
            lapisBoostAmount: Int,
            ignoredSpecies: List<String>,
            minLevel: Int,
            maxLevel: Int,
        ): ServerConfig = ServerConfig(
            maxEntranceUses = maxEntranceUses,
            shardRewards = shardRewards,
            lapisBoostAmount = lapisBoostAmount,
            ignoredSpecies = ignoredSpecies,
            minLevel = minLevel,
            maxLevel = maxLevel,
        )
    }
}
