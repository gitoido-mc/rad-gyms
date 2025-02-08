package lol.gito.radgyms.gym

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.RadGyms
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

data class GymNPC(
    val name: Text,
    val relativePosition: Vec3d,
    val yaw: Float,
)

data class GymTrainer(
    val id: String,
    val npc: GymNPC,
    val trainer: TrainerModel,
    val battleRules: BattleRules,
    val requires: String? = null
)

object GymTemplate {
    var structure: String? = null
    var relativeExitBlockSpawn: Vec3d = Vec3d.ZERO
    var relativePlayerSpawn: Vec3d = Vec3d.ZERO
    var playerYaw: Float? = null
    var trainers: List<GymTrainer> = mutableListOf()
    var type: String? = null

    fun setType(type: String): GymTemplate {
        this.type = type

        return this
    }

    fun fromGymDto(dto: GymDTO, level: Int, type: String?): GymTemplate {
        structure = dto.template
        relativeExitBlockSpawn = Vec3d(
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
            val battleConfig = if (trainer.ai.data != null) {
                RCTBattleAIConfig(
                    trainer.ai.data.moveBias ?: 1.0,
                    trainer.ai.data.statusMoveBias ?: 0.1,
                    trainer.ai.data.switchBias ?: 0.65,
                    trainer.ai.data.itemBias ?: 1.0,
                    trainer.ai.data.maxSelectMargin ?: 0.15
                )
            } else {
                RCTBattleAIConfig()
            }

            val ai = RCTBattleAI(
                battleConfig
            )
            val bag = trainer.bag.map bagMap@{ bagItem ->
                return@bagMap BagItemModel(bagItem.item, bagItem.quantity)
            }

            var pokemonCount = 1
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

            val rules = BattleRules()

            return@trainerMap GymTrainer(
                trainer.id,
                npc,
                TrainerModel(
                    Text.of(trainer.name).literalString ?: "Trainer",
                    JTO.of { ai },
                    bag,
                    team
                ),
                rules,
                trainer.requires
            )
        }

        return this
    }

    private fun generatePokemon(level: Int, type: String?): PokemonModel {
        RadGyms.LOGGER.info("Generating pokemon with level $level and type $type")
        if (type != null && type != "default") {
            RadGyms.LOGGER.info("count in bucket ${SpeciesManager.SPECIES_BY_TYPE[type]?.size}")
            val species = SpeciesManager.SPECIES_BY_TYPE[type]?.toList()?.random()!!

            RadGyms.LOGGER.info("Picked ${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level} from $type")

            return fillPokemonModel(species, level)
        } else {
            val species = PokemonSpecies.implemented.asSequence()
                .associateWith { species -> species.forms }
                .flatMap { (species, forms) ->
                    forms.map { form -> species to form }
                }.random()

            RadGyms.LOGGER.info("Picked ${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level} from random pool")

            return fillPokemonModel(species, level)
        }
    }

    private fun fillPokemonModel(species: Pair<Species, FormData>, level: Int): PokemonModel {
        val poke = Pokemon()
        poke.species = species.first
        poke.form = species.second
        poke.gender = if (Random.nextBoolean()) Gender.MALE else Gender.FEMALE
        poke.level = level
        poke.shiny = Random.nextInt(1, 10) == 1

        RadGyms.LOGGER.info("${poke.ivs.toList()}")
        val moveset = poke.moveSet.getMoves().stream().map { it.name }.toList()

        RadGyms.LOGGER.info("Moveset $moveset")

        return PokemonModel(
            poke.species.resourceIdentifier.path,
            poke.gender.asString(),
            poke.level,
            poke.nature.name.toString(),
            poke.ability.name,
            mutableSetOf(*moveset.toTypedArray()),
            PokemonModel.StatsModel(
                poke.ivs.getOrDefault(Stats.HP),
                poke.ivs.getOrDefault(Stats.ATTACK),
                poke.ivs.getOrDefault(Stats.DEFENCE),
                poke.ivs.getOrDefault(Stats.SPECIAL_ATTACK),
                poke.ivs.getOrDefault(Stats.SPECIAL_DEFENCE),
                poke.ivs.getOrDefault(Stats.SPEED),
            ),
            PokemonModel.StatsModel(
                poke.evs.getOrDefault(Stats.HP),
                poke.evs.getOrDefault(Stats.ATTACK),
                poke.evs.getOrDefault(Stats.DEFENCE),
                poke.evs.getOrDefault(Stats.SPECIAL_ATTACK),
                poke.evs.getOrDefault(Stats.SPECIAL_DEFENCE),
                poke.evs.getOrDefault(Stats.SPEED),
            ),
            poke.shiny,
            Registries.ITEM.getId(poke.heldItem().item).toString(),
            poke.aspects
        )
    }
}