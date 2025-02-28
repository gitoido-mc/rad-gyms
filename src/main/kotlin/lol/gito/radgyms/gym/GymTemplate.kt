package lol.gito.radgyms.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.toPokemon
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.CONFIG
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

data class GymNPC(
    val name: String,
    val relativePosition: Vec3d,
    val yaw: Float,
)

data class GymTrainer(
    val id: String,
    val npc: GymNPC,
    val trainer: TrainerModel,
    val battleRules: BattleRules,
    val leader: Boolean = false,
    val requires: String? = null
)

data class GymLootTable(
    val id: Identifier,
    val levels: Pair<Int, Int>
)

object GymTemplate {
    lateinit var structure: String
    var relativeExitBlockSpawn: Vec3d = Vec3d.ZERO
    var relativePlayerSpawn: Vec3d = Vec3d.ZERO
    var playerYaw: Float = 0F
    var trainers: List<GymTrainer> = mutableListOf()
    var type: String? = null
    var lootTables: List<GymLootTable> = mutableListOf()

    fun fromGymDto(dto: GymDTO, level: Int, type: String?): GymTemplate {
        structure = dto.template
        lootTables = dto.rewardLootTables.map {
            GymLootTable(
                Identifier.of(it.id),
                Pair(it.minLevel, it.maxLevel),
            )
        }
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

            val team = mutableListOf<PokemonModel>()
            if (trainer.teamType == GymTeamType.GENERATED) {
                var pokemonCount = 1

                for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
                    if (level >= mapperLevel[0]) {
                        pokemonCount = mapperLevel[1]
                    }
                }

                RadGyms.LOGGER.info("Derived pokemon count for level $level is $pokemonCount")

                val elementType: String? = if (type == "default") {
                    ElementalTypes.all().random().name
                } else null


                for (i in 1..pokemonCount) {
                    team.add(generatePokemon(level, elementType ?: type))
                }
            } else {
                for (pokeParams in trainer.team!!) {
                    val props = PokemonProperties.parse(pokeParams)
                    val poke = props.create()

                    team.add(fillPokemonModelFromPokemon(poke))
                }
            }


            val npc = GymNPC(
                name = translatable(trainer.name).string,
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
                    translatable(trainer.name).string,
                    JTO.of { ai },
                    bag,
                    team
                ),
                rules,
                trainer.leader,
                trainer.requires
            )
        }

        return this
    }

    private fun generatePokemon(level: Int, type: String?): PokemonModel {
        RadGyms.LOGGER.info("Generating pokemon with level $level and type $type")
        if (type != null && type != "default") {
            val species = SpeciesManager.SPECIES_BY_TYPE[type]?.toList()?.random()!!
            RadGyms.LOGGER.info("Picked ${species.first.showdownId()} form=${species.second.formOnlyShowdownId()} level=${level}")

            return fillPokemonModel(species, level)
        } else {
            val species = PokemonSpecies.implemented.asSequence()
                .filter { species -> species.name !in CONFIG.ignoredSpecies }
                .associateWith { species -> species.forms.filter { form -> form.name !in CONFIG.ignoredForms } }
                .flatMap { (species, forms) ->
                    forms.map { form -> species to form }
                }.random()

            RadGyms.LOGGER.info("Picked ${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level} from random pool")

            return fillPokemonModel(species, level)
        }
    }

    private fun fillPokemonModel(species: Pair<Species, FormData>, level: Int): PokemonModel {
        var pokeString =
            "${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level}"

        if (Random.nextInt(1, 10) == 1) {
            pokeString = pokeString.plus(" shiny=yes")
        }

        val pokemonProperties: PokemonProperties = pokeString.toProperties()

        // Thanks Ludichat [Cobbreeding project code]
        if (pokemonProperties.form != null)
        {
            species.first.forms.find { it.formOnlyShowdownId() == pokemonProperties.form }?.run {
                aspects.forEach {
                    // alternative form
                    pokemonProperties.customProperties.add(FlagSpeciesFeature(it, true))
                    // regional bias
                    pokemonProperties.customProperties.add(
                        StringSpeciesFeature(
                            "region_bias",
                            it.split("-").last()
                        )
                    )
                    // Basculin wants to be special
                    // We're handling aspects now but some form handling should be kept to prevent
                    // legitimate abilities to be flagged as forced
                    pokemonProperties.customProperties.add(
                        StringSpeciesFeature(
                            "fish_stripes",
                            it.removeSuffix("striped")
                        )
                    )
                }
            }
        }

        val poke = pokemonProperties.create()
        poke.setFriendship(120)
        poke.forcedAspects = pokemonProperties.aspects

        return fillPokemonModelFromPokemon(poke)
    }

    private fun fillPokemonModelFromPokemon(poke: Pokemon): PokemonModel = PokemonModel(
        poke.species.resourceIdentifier.path,
        poke.gender.toString(),
        poke.level,
        poke.nature.name.path,
        poke.ability.name,
        poke.moveSet.map { it.name }.toSet(),
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
        poke.heldItem().registryEntry.idAsString,
        poke.aspects
    )
}