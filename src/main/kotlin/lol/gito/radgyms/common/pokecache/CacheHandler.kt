/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.pokecache

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_RARITY
import lol.gito.radgyms.common.util.isShiny
import lol.gito.radgyms.common.util.shinyRoll
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.behavior.ShufflingList
import net.minecraft.world.item.Rarity
import kotlin.random.Random

object CacheHandler {

    // Valid stats only to prevent crash (No Accuracy/Evasion)
    private val validStats = listOf(
        Stats.HP, Stats.ATTACK, Stats.DEFENCE,
        Stats.SPECIAL_ATTACK, Stats.SPECIAL_DEFENCE, Stats.SPEED
    )

    fun getPoke(
        type: String,
        rarity: Rarity,
        player: ServerPlayer,
        shinyBoost: Int? = 0
    ): Pokemon = getPoke(
        ElementalTypes.get(type) ?: ElementalTypes.all().random(),
        rarity,
        player,
        shinyBoost
    )

    fun getPoke(
        type: ElementalType,
        rarity: Rarity,
        player: ServerPlayer,
        shinyBoost: Int? = 0
    ): Pokemon {
        // 1. GET CONFIG
        val tierKey = rarity.name.lowercase() // "common", "uncommon", etc.
        val config = RadGyms.CONFIG.cacheSettings?.get(tierKey)
            ?: throw IllegalStateException("Missing config for cache tier: $tierKey")

        // 2. BUILD POOL FROM CONFIG
        // Looks up specific elemental cache data and merges lists based on config.poolSources
        val cacheDTO = SPECIES_BY_RARITY[type.showdownId] ?: throw IllegalStateException("No data for type ${type.showdownId}")
        val combinedPool = ShufflingList<String>()

        config.poolSources.forEach { source ->
            val mapToAdd = when(source.lowercase()) {
                "common" -> cacheDTO.common
                "uncommon" -> cacheDTO.uncommon
                "rare" -> cacheDTO.rare
                "epic" -> cacheDTO.epic
                else -> emptyMap()
            }

            mapToAdd.forEach { (id, weight) ->
                // Prevent adding duplicates if they already exist
                // ShufflingList doesn't have a straightforward contains check,
                // but generally the high weights and separate lists make duplicates rare/negligible
                combinedPool.add(id, weight)
            }
        }

        // Safety fallback
        if(combinedPool.toString().contains("[]")) {
            // If empty (e.g., user deleted data), define a safe fallback
            combinedPool.add("ditto", 1)
        }

        // 3. GENERATE POKEMON
        var poke: Pokemon?
        do {
            val pokeProps = PokemonProperties.parse(combinedPool.shuffle().first())
            poke = pokeProps.create()
        } while (!poke.species.implemented)

        // 4. SET LEVEL FROM CONFIG
        poke.level = config.level

        // 5. SET IVS FROM CONFIG
        val roll = Random.nextDouble(0.0, 100.0)

        // Find the first rule where the roll meets the requirement (assumes config rules are logical)
        // We sort by chance ascending (e.g. 5, 15, 50, 100) so if we roll 3, we hit the 5 rule.
        val rule = config.ivRules
            .sortedBy { it.chance }
            .firstOrNull { roll <= it.chance }

        val perfectCount = rule?.minPerfectIVs ?: 1

        // Apply 31 IVs to random stats
        validStats.shuffled().take(perfectCount).forEach { stat ->
            poke.ivs[stat] = 31
        }

        // 6. SHINY AND FINALIZE
        poke.shiny = shinyRoll(poke, player, shinyBoost).isShiny()
        poke.updateAspects()
        poke.updateForm()

        return poke.initialize()
    }
}