/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
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
    private typealias ElementalListType = List<@Serializable(ElementalTypeSerializer::class) ElementalType>

    data class EntityData(
        val name: Component,
        val relativePosition: Vec3,
        val yaw: Float,
    )

    data object Json {
        @Serializable
        data class Trainer(
            val id: String,
            var name: String = "rad_gyms.npc.default_trainer",
            @SerialName("spawn_relative")
            val spawnRelative: Gym.Json.EntityCoordsAndYaw,
            @SerialName("team_type")
            val teamType: GymTeamType = GymTeamType.GENERATED,
            @SerialName("team_generator")
            val teamGenerator: GymTeamGeneratorType = GymTeamGeneratorType.CHAOTIC,
            @Contextual
            @SerialName("possible_elemental_types")
            val possibleElementalTypes: ElementalListType = listOf(
                ElementalTypes.getRandomType()
            ),
            @SerialName("possible_formats")
            val possibleFormats: List<GymBattleFormat> = listOf(GymBattleFormat.SINGLES),
            val ai: AI = AI(),
            val bag: List<Bag> = listOf(
                Bag("cobblemon:hyper_potion", 2)
            ),
            @SerialName("level_thresholds")
            val countPerLevelThreshold: List<Threshold> = listOf(
                Threshold(3, 25),
                Threshold(4, 50),
                Threshold(5, 75),
                Threshold(6, 100),
            ),
            @SerialName("battle_rules")
            val battleRules: BattleRules = BattleRules(),
            val team: List<String>? = null,
            val leader: Boolean = false,
            val requires: String? = null
        ) {
            init {
                teamType.let {
                    when (teamType) {
                        GymTeamType.GENERATED -> {}
                        GymTeamType.FIXED -> {
                            requireNotNull(team)
                            require(team.count() in 1..6)
                        }
                        GymTeamType.POOL -> {
                            requireNotNull(team)
                            require(team.isNotEmpty() && team.count() > 0)
                        }
                    }
                }
            }
        }

        @Serializable
        data class BattleRules(
            @SerialName("max_item_uses")
            val maxItemUses: Int = 5
        )

        @Serializable
        data class AI(
            val type: String = "rct",
            val data: Config? = null,
        ) {
            @Serializable
            data class Config(
                // RCTBattleAI params
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
                // StrongBattleAI
                @SerialName("skill_level")
                val skillLevel: Int? = null
            )
        }

        @Serializable
        data class Bag(
            val item: String,
            val quantity: Int
        )

        @Serializable
        data class Threshold(
            val amount: Int,
            @SerialName("until_level")
            val untilLevel: Int
        )
    }
}