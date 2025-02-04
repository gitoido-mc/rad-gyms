package lol.gito.radgyms.pokemon

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Species
import lol.gito.radgyms.RadGyms

object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, List<Pair<Species, FormData>>> = HashMap(ElementalTypes.count())

    fun register() {
        CobblemonEvents.DATA_SYNCHRONIZED.subscribe(Priority.NORMAL) { _ ->
            RadGyms.LOGGER.info("Cobblemon data sync")
            this.updateSpeciesMap()
        }
        PokemonSpecies.observable.subscribe(Priority.NORMAL) { _ ->
            RadGyms.LOGGER.info("Species observable updated")
            this.updateSpeciesMap()
        }
    }

    private fun speciesOfType(elementalType: ElementalType): List<Pair<Species, FormData>> {
        return PokemonSpecies.implemented.asSequence()
            .associateWith { species -> species.forms }
            .flatMap { (species, forms) ->
                forms.filter { form -> form.types.contains(elementalType) }
                    .map { form -> species to form }
            }
            .toList()
    }

    private fun updateSpeciesMap() {
        for (type in ElementalTypes.all()) {
            SPECIES_BY_TYPE[type.name] = speciesOfType(type)
            RadGyms.LOGGER.info("Type ${type.name} count is ${SPECIES_BY_TYPE[type.name]?.size}")
        }
        RadGyms.LOGGER.info("count is ${SPECIES_BY_TYPE.size}")
    }
}