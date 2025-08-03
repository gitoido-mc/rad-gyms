/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

@file:Suppress("unused")

package lol.gito.radgyms.common.gym

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GymTeamType {
    GENERATED, FIXED
}

@Serializable
data class GymCoordsAndYawDTO(
    val pos: List<Double>,
    val yaw: Double,
)

@Serializable
data class GymTrainerAIDataDTO(
    val moveBias: Double? = null,
    val statusMoveBias: Double? = null,
    val switchBias: Double? = null,
    val itemBias: Double? = null,
    val maxSelectMargin: Double? = null,
)

@Serializable
data class GymTrainerBattleRulesDTO(
    val maxItemUses: Int
)

@Serializable
data class GymTrainerAIDTO(
    val type: String,
    val data: GymTrainerAIDataDTO? = null,
)

@Serializable
data class GymTrainerBagItemDTO(
    val item: String,
    val quantity: Int
)

@Serializable
data class GymTrainerDTO(
    val id: String,
    val name: String,
    @SerialName("spawn_relative")
    val spawnRelative: GymCoordsAndYawDTO,
    @SerialName("team_type")
    val teamType: GymTeamType,
    val ai: GymTrainerAIDTO,
    val bag: List<GymTrainerBagItemDTO> = emptyList(),
    @SerialName("count_per_level_threshold")
    val countPerLevelThreshold: List<List<Int>> = listOf(
        listOf(20, 3),
        listOf(40, 4),
        listOf(60, 5),
        listOf(80, 6),
    ),
    val battleRules: GymTrainerBattleRulesDTO? = null,
    val team: List<String>? = null,
    val leader: Boolean = false,
    val requires: String? = null,
) {
    init {
        if (teamType == GymTeamType.FIXED) {
            requireNotNull(team)
        }
    }
}

@Serializable
data class GymLootTableDTO(
    val id: String,
    val minLevel: Int = 1,
    val maxLevel: Int = 100,
)

@Serializable
data class GymDTO(
    @SerialName("interior_template")
    val template: String,
    @SerialName("exit_block_pos")
    val exitBlockPos: List<Double>,
    @SerialName("reward_loot_tables")
    val rewardLootTables: List<GymLootTableDTO>,
    @SerialName("player_spawn_relative")
    val playerSpawnRelative: GymCoordsAndYawDTO,
    val trainers: List<GymTrainerDTO>
)