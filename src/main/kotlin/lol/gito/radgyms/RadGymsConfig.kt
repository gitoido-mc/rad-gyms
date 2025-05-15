package lol.gito.radgyms

import kotlinx.serialization.Serializable

@Serializable
class RadGymsConfig(
    val debug: Boolean = false,
    val maxEntranceUses: Int = 3,
    val lapisBoostAmount: Int = 1,
    val shardRewards: Boolean = true,
    val ignoredSpecies: List<String> = mutableListOf(),
    val ignoredForms: List<String> = mutableListOf("gmax"),
)
