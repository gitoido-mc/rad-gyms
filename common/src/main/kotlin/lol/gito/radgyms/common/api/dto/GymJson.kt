/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.RadGyms

@Serializable
data class GymJson(
    val id: String,
    @SerialName("interior_template")
    val template: String,
    @SerialName("exit_block_pos")
    val exitBlockPos: Coords,
    @SerialName("player_spawn_relative")
    val playerSpawnRelative: EntityCoordsAndYaw,
    @SerialName("trainers")
    val trainers: List<Trainer>,
    @SerialName("reward_loot_tables")
    val rewardLootTables: List<LootTableInfo>? = listOf(
        LootTableInfo(
            id = "rad_gyms:gyms/default/shared_loot_table",
            minLevel = 1,
            maxLevel = RadGyms.CONFIG.maxLevel!!
        ),
        LootTableInfo(
            id = "rad_gyms:gyms/default/common_loot_table",
            minLevel = 1,
            maxLevel = 25
        ),
        LootTableInfo(
            id = "rad_gyms:gyms/default/uncommon_loot_table",
            minLevel = 26,
            maxLevel = 50
        ),
        LootTableInfo(
            id = "rad_gyms:gyms/default/rare_loot_table",
            minLevel = 51,
            maxLevel = 75
        ),
        LootTableInfo(
            id = "rad_gyms:gyms/default/epic_loot_table",
            minLevel = 76
        ),
    )
)
