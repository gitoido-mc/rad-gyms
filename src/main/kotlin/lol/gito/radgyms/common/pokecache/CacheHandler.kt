/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.pokecache

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.text.add
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.lang
import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_RARITY
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity
import kotlin.random.Random

object CacheHandler {
    private fun Float.checkRate(): Boolean =
        if (this >= 1) (Random.Default.nextFloat() < 1 / this) else Random.Default.nextFloat() < this


    fun getPoke(
        type: String,
        rarity: Rarity,
        player: ServerPlayerEntity,
        shinyBoost: Int? = 0,
        addToParty: Boolean? = false
    ): Pokemon = getPoke(
        ElementalTypes.get(type) ?: ElementalTypes.all().random(),
        rarity,
        player,
        shinyBoost,
        addToParty
    )

    fun getPoke(
        type: ElementalType,
        rarity: Rarity,
        player: ServerPlayerEntity,
        shinyBoost: Int? = 0,
        addToParty: Boolean? = false
    ): Pokemon {
        val cache = SPECIES_BY_RARITY[type.name]!!.forRarity(rarity)

        val pokeProps = PokemonProperties.parse(cache.shuffle().first())
        val poke = pokeProps.create()

        poke.shiny = shinyRoll(poke, player, shinyBoost).checkRate()
        val hasShinyCharm = player.inventory.contains(
            TagKey.of(
                RegistryKeys.ITEM,
                modId("items/shiny_chance_items")
            )
        )

        if (!poke.shiny && hasShinyCharm) {
            poke.shiny = shinyRoll(poke, player, shinyBoost).checkRate()
        }

        poke.updateAspects()
        poke.updateForm()

        val instance = poke.initialize()

        if (addToParty == true) {
            player.party().add(instance)
        }

        return instance
    }

    private fun shinyRoll(poke: Pokemon, player: ServerPlayerEntity, shinyBoost: Int? = 0): Float {
        var shinyRate = Cobblemon.config.shinyRate - (shinyBoost ?: 0).toFloat()
        val event = ShinyChanceCalculationEvent(shinyRate, poke)
        CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
            shinyRate = it.calculate(player)
        }
        if (shinyRate == 0f) return 1f
        return shinyRate
    }

    fun getPokeNames(type: ElementalType, rarity: Rarity?): List<MutableText> {
        val pokes = mutableListOf<MutableText>()

        val pokeRarity = when (rarity) {
            null -> Rarity.entries.random()
            else -> rarity
        }

        SPECIES_BY_RARITY[type.name]!!.forRarity(pokeRarity).toList().shuffled().take(5).forEach { record ->
            val pokeProps = PokemonProperties.parse(record)
            val poke = pokeProps.create()

            poke.updateAspects()
            poke.updateForm()

            if (poke.form.name != poke.species.standardForm.name) {
                val id = poke.form.name.lowercase()
                val form = lang("ui.pokedex.info.form.$id")
                val pokeName = lang("species.${poke.species.showdownId().lowercase()}.name")
                    .add(" ")
                    .add("(")
                    .add(form.string)
                    .add(")")
                    .styled { it.withFormatting(pokeRarity.formatting) }

                pokes.add(pokeName)
            }
            pokes.add(
                translatable(
                    cobblemonResource("species.${poke.species.showdownId().lowercase()}.name").toTranslationKey()
                ).styled { it.withFormatting(pokeRarity.formatting) }
            )
        }

        return pokes
    }
}
