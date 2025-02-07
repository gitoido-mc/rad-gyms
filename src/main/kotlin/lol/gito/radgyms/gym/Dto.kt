@file:Suppress("unused")

package lol.gito.radgyms.gym

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

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
data class GymTrainerTeamMemberDTO(
    val species: String,
    val gender: String? = null,
    val nature: String? = null,
    val ability: String? = null,
    val moveset: Set<String>? = null,
    val ivs: Map<String, Int>? = null,
    val evs: Map<String, Int>? = null,
    val heldItem: String? = null
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
    val bag: List<GymTrainerBagItemDTO>,
    @SerialName("count_per_level_threshold")
    val levelToCount: List<List<Int>>,
    val battleRules: GymTrainerBattleRulesDTO? = null,
    val team: List<GymTrainerTeamMemberDTO>? = null,
    val requires: String? = null,
) {
    init {
        if (teamType == GymTeamType.FIXED) {
            requireNotNull(team)
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GymDTO(
    @SerialName("interior_template")
    val template: String,
    @SerialName("exit_block_pos")
    val exitBlockPos: List<Double>,
    @SerialName("player_spawn_relative")
    val playerSpawnRelative: GymCoordsAndYawDTO,
    val trainers: List<GymTrainerDTO>
)