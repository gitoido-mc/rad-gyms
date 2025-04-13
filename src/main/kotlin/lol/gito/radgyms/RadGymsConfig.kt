package lol.gito.radgyms

import kotlinx.serialization.Serializable

@Serializable
data class RadGymsConfig(
    val debug: Boolean? = null,
    val maxEntranceUses: Int? = null,
    val shardRewards: Boolean = true,
    val ignoredSpecies: List<String>? = null,
    val ignoredForms: List<String>? = null,
) {
    fun combine(other: RadGymsConfig): RadGymsConfig {
        return this.copy(
            debug = other.debug ?: debug,
            maxEntranceUses = other.maxEntranceUses ?: maxEntranceUses,
            shardRewards = other.shardRewards ?: shardRewards,
            ignoredSpecies = other.ignoredSpecies ?: ignoredSpecies,
            ignoredForms = other.ignoredForms ?: ignoredForms
        )
    }
}
