package lol.gito.radgyms.common.gym


import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import lol.gito.radgyms.common.gym.SpeciesManager.generatePokemon
import net.minecraft.server.level.ServerPlayer

class TeamGenerator {
    fun generate(
        trainer: TrainerModel.Json.Trainer,
        level: Int,
        elementType: String,
        player: ServerPlayer,
        possibleFormats: MutableList<GymBattleFormat>
    ): MutableList<PokemonModel> {
        var pokemonCount = 1
        for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
            if (level >= mapperLevel[0]) pokemonCount = mapperLevel[1]
        }

        val rawTeam = mutableListOf<PokemonProperties>()
        val event = GymEvents.GenerateTeamEvent(
            player,
            elementType,
            level,
            trainer.id,
            trainer.leader,
            rawTeam,
            possibleFormats
        )

        debug("Ignored species: ${CONFIG.ignoredSpecies}")
        debug("Ignored species: ${CONFIG.ignoredForms}")
        (1..pokemonCount).forEach { _ ->
            rawTeam.add(generatePokemon(level, elementType))
        }

        val team = mutableListOf<PokemonModel>()
        GENERATE_TEAM.post(event) { ev ->
            ev.team.forEach { props ->
                team.add(fillPokemonModelFromPokemon(props))
            }
        }
        return team
    }
}