package lol.gito.radgyms.gym

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.pokemon.SpeciesManager
import lol.gito.radgyms.resource.Gym
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import java.util.function.Supplier

data class GymNPC(
    val name: Text,
    val relativePosition: Vec3d,
    val yaw: Float,
)

object GymTemplate {
    var structure: String? = null
    var relativeExitBLockSpawn: Vec3d? = null
    var relativePlayerSpawn: Vec3d? = null
    var playerYaw: Float? = null
    var trainers: List<Triple<String, GymNPC, TrainerModel>> = mutableListOf()
    var type: String? = null

    fun setType(type: String): GymTemplate {
        this.type = type

        return this
    }

    fun fromGymDto(dto: Gym, level: Int, type: String?): GymTemplate {
        structure = dto.template
        relativeExitBLockSpawn = Vec3d(
            dto.exitBlockPos[0],
            dto.exitBlockPos[1],
            dto.exitBlockPos[2],
        )

        relativePlayerSpawn = Vec3d(
            dto.playerSpawnRelative.pos[0],
            dto.playerSpawnRelative.pos[1],
            dto.playerSpawnRelative.pos[2],
        )
        playerYaw = dto.playerSpawnRelative.yaw.toFloat()

        trainers = dto.trainers.map trainerMap@{ trainer ->
            val ai = RCTBattleAI(
                RCTBattleAIConfig(
                    /* moveBias = */ 1.0,
                    /* statusMoveBias = */ 0.1,
                    /* switchBias = */ 0.65,
                    /* itemBias = */ 1.0,
                    /* maxSelectMargin = */ trainer.ai.data.maxSelectMargin
                )
            )
            val bag = trainer.bag.map bagMap@{ bagItem ->
                return@bagMap BagItemModel(bagItem.item, bagItem.quantity)
            }

            var pokemonCount: Int = 1
            for (mapperLevel in trainer.levelToCount) {
                if (level <= mapperLevel[0]) {
                    pokemonCount = mapperLevel[1]
                    break
                }
            }

            val team = mutableListOf<PokemonModel>()
            for (i in 1..pokemonCount) {
                team.add(generatePokemon(level, type))
            }

            val npc = GymNPC(
                name = Text.translatable(trainer.name),
                relativePosition = Vec3d(
                    trainer.spawnRelative.pos[0],
                    trainer.spawnRelative.pos[1],
                    trainer.spawnRelative.pos[2]
                ),
                yaw = trainer.spawnRelative.yaw.toFloat(),
            )

            return@trainerMap Triple(
                trainer.id,
                npc,
                TrainerModel(
                    trainer.name,
                    JTO.of { ai },
                    bag,
                    team
                )
            )
        }

        return this
    }

    private fun generatePokemon(level: Int, type: String?): PokemonModel {
        val pokemonSpecies: Species = if (type != null) {
            SpeciesManager.SPECIES_BY_TYPE[type]?.toList()?.random()!!
        } else {
            PokemonSpecies.implemented.random()
        }

        return PokemonModel(pokemonSpecies.create(level))
    }
}