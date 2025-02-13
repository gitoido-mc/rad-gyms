package lol.gito.radgyms

import kotlinx.serialization.Serializable

@Serializable
data class RadGymsConfig(
    val debug: Boolean,
    val maxEntranceUses: Int,
    val ignoredSpecies: List<String>,
    val ignoredForms: List<String>
)