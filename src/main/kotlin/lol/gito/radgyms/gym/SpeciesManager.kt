package lol.gito.radgyms.gym

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Species
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGyms.debug

object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, List<Pair<Species, FormData>>> = HashMap(ElementalTypes.count())

    fun speciesOfType(elementalType: ElementalType): List<Pair<Species, FormData>> {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies
            .filter { filterSpecies -> filterSpecies.name !in CONFIG.ignoredSpecies }
            .associateWith { associateSpecies -> associateSpecies.forms.filter { form -> form.name !in CONFIG.ignoredForms } }
            .flatMap { (flatMapSpecies, forms) ->
                forms.filter { form -> form.types.contains(elementalType) }
                    .map { form -> flatMapSpecies to form }
            }
            .toList()

        if (species.isNotEmpty()) return species

        return allSpecies
            .filter { filterSpecies -> filterSpecies.name !in CONFIG.ignoredSpecies }
            .associateWith { associateSpecies -> associateSpecies.forms.filter { form -> form.name !in CONFIG.ignoredForms } }
            .flatMap { (flatMapSpecies, forms) ->
                forms.map { form -> flatMapSpecies to form }
            }
            .toList()
    }

    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
