/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.util.math.BlockPos
import java.util.*

data class Gym(
    val template: GymTemplate,
    val npcList: Map<UUID, TrainerModel>,
    val coords: BlockPos,
    val level: Int,
    val type: String,
    val label: String
) {
    @Serializable
    data class Json(
        @SerialName("interior_template")
        val template: String,
        @SerialName("exit_block_pos")
        val exitBlockPos: List<Double>,
        @SerialName("reward_loot_tables")
        val rewardLootTables: List<LootTableInfo>,
        @SerialName("player_spawn_relative")
        val playerSpawnRelative: EntityCoordsAndYaw,
        @SerialName("trainers")
        val trainers: List<TrainerModel.Json.Trainer>
    ) {
        @Serializable
        data class EntityCoordsAndYaw(
            val pos: List<Double>,
            val yaw: Double,
        )

        @Serializable
        data class LootTableInfo(
            val id: String,
            val minLevel: Int = 1,
            val maxLevel: Int = 100
        )
    }
}