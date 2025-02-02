package lol.gito.radgyms.resource

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
enum class GymTeamType {
    GENERATED, FIXED
}

@Serializable
data class GymCoordsAndYaw(
    val pos: List<Double>,
    val yaw: Double,
)

@Serializable
data class GymTrainerTeamMember(
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
data class GymTrainerAIData(
    val maxSelectMargin: Double
)

@Serializable
data class GymTrainerBattleRules(
    val maxItemUses: Int
)

@Serializable
data class GymTrainerAI(
    val type: String,
    val data: GymTrainerAIData
)

@Serializable
data class GymTrainerBagItem(
    val item: String,
    val quantity: Int
)

@Serializable
data class GymTrainer(
    val id: String,
    val name: String,
    @SerialName("spawn_relative")
    val spawnRelative: GymCoordsAndYaw,
    @SerialName("team_type")
    val teamType: GymTeamType,
    val ai: GymTrainerAI,
    val bag: List<GymTrainerBagItem>,
    @SerialName("count_per_level_threshold")
    val levelToCount: List<Pair<Int, Int>>,
    val battleRules: GymTrainerBattleRules? = null,
    val team: List<GymTrainerTeamMember>? = null,
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
data class Gym(
    @SerialName("interior_template")
    val template: String,
    @SerialName("exit_block_pos")
    val exitBlockPos: List<Double>,
    @SerialName("player_spawn_relative")
    val playerSpawnRelative: GymCoordsAndYaw,
    val trainers: List<GymTrainer>
)