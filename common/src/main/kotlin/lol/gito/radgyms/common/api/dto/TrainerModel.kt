/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto

import com.cobblemon.mod.common.api.types.ElementalType
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.api.serialization.ElementalTypeSerializer
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

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
        val name: Component,
        val relativePosition: Vec3,
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
            @SerialName("team_generator")
            val teamGenerator: GymTeamGeneratorType? = GymTeamGeneratorType.BST,
            @Contextual
            @SerialName("possible_elemental_types")
            val possibleElementalTypes: List<@Serializable(ElementalTypeSerializer::class) ElementalType>? = null,
            @SerialName("possible_formats")
            val possibleFormats: List<GymBattleFormat> = listOf(GymBattleFormat.SINGLES),
            val ai: AI,
            val bag: List<Bag> = emptyList(),
            @SerialName("level_thresholds")
            val countPerLevelThreshold: List<List<Int>> = listOf(
                listOf(20, 3),
                listOf(40, 4),
                listOf(60, 5),
                listOf(80, 6),
            ),
            @SerialName("battle_rules")
            val battleRules: BattleRules? = null,
            val team: List<String>? = null,
            val leader: Boolean = false,
            val requires: String? = null,
        ) {
            init {
                when (teamType) {
                    GymTeamType.GENERATED -> {
                        requireNotNull(possibleElementalTypes)
                        requireNotNull(teamGenerator)
                    }

                    GymTeamType.POOL, GymTeamType.FIXED -> {
                        requireNotNull(team)
                    }
                }
            }
        }

        @Serializable
        data class BattleRules(
            @SerialName("max_item_uses")
            val maxItemUses: Int
        )

        @Serializable
        data class AI(
            val type: String,
            val data: Config? = null,
        ) {
            @Serializable
            data class Config(
                @SerialName("move_bias")
                val moveBias: Double? = null,
                @SerialName("status_move_bias")
                val statusMoveBias: Double? = null,
                @SerialName("switch_bias")
                val switchBias: Double? = null,
                @SerialName("item_bias")
                val itemBias: Double? = null,
                @SerialName("max_select_margin")
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