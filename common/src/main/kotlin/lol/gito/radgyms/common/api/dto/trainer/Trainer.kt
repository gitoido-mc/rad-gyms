/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.trainer

import com.cobblemon.mod.common.api.types.ElementalTypes
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.*
import lol.gito.radgyms.common.api.dto.battle.BattleAI
import lol.gito.radgyms.common.api.dto.battle.BattleRules
import lol.gito.radgyms.common.api.dto.geospatial.EntityCoordsAndYaw
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.api.serialization.ElementalListType

@Serializable
data class Trainer(
    val id: String,
    var name: String = "rad_gyms.npc.default_trainer",
    @SerialName("spawn_relative")
    val spawnRelative: EntityCoordsAndYaw,
    @SerialName("type")
    val teamType: GymTeamType = GymTeamType.GENERATED,
    @SerialName("generator")
    val teamGenerator: GymTeamGeneratorType = GymTeamGeneratorType.CHAOTIC,
    @Contextual
    @SerialName("elemental_types")
    val possibleElementalTypes: ElementalListType = listOf(
        ElementalTypes.getRandomType()
    ),
    @SerialName("possible_formats")
    val possibleFormats: List<GymBattleFormat> = listOf(GymBattleFormat.SINGLES),
    val ai: BattleAI = BattleAI(),
    val bag: List<TrainerBag> = listOf(
        TrainerBag("cobblemon:hyper_potion", 2)
    ),
    @SerialName("level_thresholds")
    val countPerLevelThreshold: List<TeamLevelThreshold> = listOf(
        TeamLevelThreshold(T1_TEAM_SIZE, T1_TEAM_LEVEL_THRESHOLD),
        TeamLevelThreshold(T2_TEAM_SIZE, T2_TEAM_LEVEL_THRESHOLD),
        TeamLevelThreshold(T3_TEAM_SIZE, T3_TEAM_LEVEL_THRESHOLD),
        TeamLevelThreshold(T4_TEAM_SIZE, T4_TEAM_LEVEL_THRESHOLD),
    ),
    @SerialName("battle_rules")
    val battleRules: BattleRules = BattleRules(),
    val team: List<String>? = null,
    val leader: Boolean = false,
    val requires: String? = null
) {
    companion object {
        const val T1_TEAM_SIZE = 3
        const val T2_TEAM_SIZE = 4
        const val T3_TEAM_SIZE = 5
        const val T4_TEAM_SIZE = 6

        const val T1_TEAM_LEVEL_THRESHOLD = 25
        const val T2_TEAM_LEVEL_THRESHOLD = 50
        const val T3_TEAM_LEVEL_THRESHOLD = 75
        const val T4_TEAM_LEVEL_THRESHOLD = 100
    }

    init {
        teamType.let {
            when (teamType) {
                GymTeamType.GENERATED -> {}
                GymTeamType.FIXED -> {
                    requireNotNull(team)
                    require(team.count() in 1..MAX_PARTY_SIZE)
                }

                GymTeamType.POOL -> {
                    requireNotNull(team)
                    require(team.isNotEmpty() && team.count() > 0)
                }
            }
        }
    }
}
