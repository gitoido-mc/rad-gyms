/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
@file:Suppress("LongParameterList")

package lol.gito.radgyms.common.config

import lol.gito.radgyms.common.RadGyms.debug
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

@Suppress("MagicNumber")
class ServerConfig(
    // Debug logging
    val debug: Boolean = false,

    // Gym entrance max uses per player
    val maxEntranceUses: Int = 3,

    // Should average team level be derived automatically
    val deriveAverageGymLevel: Boolean = true,

    // Gym level bounds
    val minLevel: Int = 10,
    val maxLevel: Int = 100,

    // Add shard rewards
    val shardRewards: Boolean = true,

    // Cache shiny boost amount per unit of lapis
    val cacheBoosters: MutableMap<String, Int> = mutableMapOf(
        "minecraft:lapis_lazuli" to 1,
        "minecraft:lapis_block" to 9,
    ),

    // Ignored species
    val ignoredSpecies: List<String> = listOf(
        "mega",
        "mega-x",
        "mega-y",
        "gmax",
    ),

    val trainerTeamShinyChance: Int = 10,
) {
    val boosterMap: Map<Item, Int> by lazy {
        this.cacheBoosters.mapKeys {
            BuiltInRegistries.ITEM.get(ResourceLocation.parse(it.key))
        }.also {
            debug("Loaded ${this.cacheBoosters.count()} cache boosters")
        }
    }

    companion object {
        fun create(
            maxEntranceUses: Int,
            shardRewards: Boolean,
            ignoredSpecies: List<String>,
            minLevel: Int,
            maxLevel: Int,
        ): ServerConfig = ServerConfig(
            maxEntranceUses = maxEntranceUses,
            shardRewards = shardRewards,
            ignoredSpecies = ignoredSpecies,
            minLevel = minLevel,
            maxLevel = maxLevel,
        )
    }

    fun warmupBoosters() {
//        if (!this::boosterMap.isInitialized) {
//            this.boosterMap =
//        }
    }
}
