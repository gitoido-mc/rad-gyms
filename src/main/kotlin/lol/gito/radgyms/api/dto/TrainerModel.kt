/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.dto

import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.api.enumeration.GymBattleFormat
import lol.gito.radgyms.api.enumeration.GymTeamType
import net.minecraft.util.math.Vec3d

data class TrainerModel(
    val id: String,
    val npc: EntityData,
    val trainer: TrainerModel,
    val battleRules: BattleRules,
    val format: GymBattleFormat = GymBattleFormat.SINGLES,
    val leader: Boolean = false,
    val requires: String? = null
) {
    data class EntityData(
        val name: String,
        val relativePosition: Vec3d,
        val yaw: Float,
    )

    data object Json {

        @Serializable
        data class Trainer(
            val id: String,
            val name: String,
            @SerialName("spawn_relative")
            val spawnRelative: Gym.Json.EntityCoordsAndYaw,
            @SerialName("team_type")
            val teamType: GymTeamType,
            val ai: AI,
            val bag: List<Bag> = emptyList(),
            @SerialName("count_per_level_threshold")
            val countPerLevelThreshold: List<List<Int>> = listOf(
                listOf(20, 3),
                listOf(40, 4),
                listOf(60, 5),
                listOf(80, 6),
            ),
            val battleRules: BattleRules? = null,
            val team: List<String>? = null,
            val possibleFormats: List<GymBattleFormat> = listOf(GymBattleFormat.SINGLES),
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
        data class BattleRules(
            val maxItemUses: Int
        )

        @Serializable
        data class AI(
            val type: String,
            val data: Config? = null,
        ) {
            @Serializable
            data class Config(
                val moveBias: Double? = null,
                val statusMoveBias: Double? = null,
                val switchBias: Double? = null,
                val itemBias: Double? = null,
                val maxSelectMargin: Double? = null,
            )
        }

        @Serializable
        data class Bag(
            val item: String,
            val quantity: Int
        )

    }
}