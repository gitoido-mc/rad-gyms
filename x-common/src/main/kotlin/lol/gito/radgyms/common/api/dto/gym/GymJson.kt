/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.gym

import com.google.gson.annotations.SerializedName
import lol.gito.radgyms.common.api.dto.geospatial.Coords
import lol.gito.radgyms.common.api.dto.geospatial.EntityCoordsAndYaw
import lol.gito.radgyms.common.api.dto.reward.LootTableReward
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import lol.gito.radgyms.common.api.dto.trainer.Trainer

data class GymJson(
    val id: String,
    @SerializedName("interior_template")
    val template: String,
    @SerializedName("exit_block_pos")
    val exitBlockPos: Coords,
    @SerializedName("player_spawn_relative")
    val playerSpawnRelative: EntityCoordsAndYaw,
    val trainers: List<Trainer>,
    val rewards: List<RewardInterface> =
        listOf(
            LootTableReward(
                id = "rad_gyms:gyms/default/shared_loot_table",
            ),
            LootTableReward(
                id = "rad_gyms:gyms/default/common_loot_table",
                maxLevel = 25,
            ),
            LootTableReward(
                id = "rad_gyms:gyms/default/uncommon_loot_table",
                minLevel = 26,
                maxLevel = 50,
            ),
            LootTableReward(
                id = "rad_gyms:gyms/default/rare_loot_table",
                minLevel = 51,
                maxLevel = 75,
            ),
            LootTableReward(
                id = "rad_gyms:gyms/default/epic_loot_table",
                minLevel = 76,
            ),
        ),
)
