package lol.gito.radgyms

import kotlinx.serialization.Serializable

class RadGymsConfig {
    @Serializable
    companion object ConfigContainer {
        var debug: Boolean = false
        var maxEntranceUses: Int = 3
        var ignoredSpecies: List<String> = emptyList()
        var ignoredForms: List<String> = emptyList()
    }
}