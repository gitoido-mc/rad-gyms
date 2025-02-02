package lol.gito.radgyms.pokemon

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Species

object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, List<Species>> = HashMap(ElementalTypes.count())

    fun register() {
        for (type in ElementalTypes.all()) {
            SPECIES_BY_TYPE[type.name] = speciesOfType(type)
        }
    }

    private fun speciesOfType(elementalType: ElementalType): List<Species> {
        return PokemonSpecies.implemented.asSequence()
            .filter { pokemon -> pokemon.types.contains(elementalType) }
            .toList()
    }


}