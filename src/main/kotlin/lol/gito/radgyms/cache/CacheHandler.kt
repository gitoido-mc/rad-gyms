package lol.gito.radgyms.cache

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.text.add
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.lang
import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.gym.SpeciesManager.SPECIES_BY_RARITY
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity
import java.util.*
import kotlin.random.Random

object CacheHandler {
    private fun Float.checkRate(): Boolean =
        if (this >= 1) (Random.Default.nextFloat() < 1 / this) else Random.Default.nextFloat() < this

    @Environment(EnvType.SERVER)
    fun getPoke(
        type: ElementalType,
        rarity: Rarity,
        playerUUID: UUID,
        shinyBoost: Int? = 0,
        addToParty: Boolean? = false
    ): Pokemon {
        val player = Cobblemon.implementation.server()!!.playerManager.getPlayer(playerUUID)
        return getPoke(type, rarity, player!!, shinyBoost, addToParty)
    }

    @Environment(EnvType.SERVER)
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
        val hasShinyCharm = player.inventory.contains {
            it.registryEntry.idAsString == "unimplemented_items:shiny_charm" // TODO: Make it taggable?
        }

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

    @Environment(EnvType.SERVER)
    private fun shinyRoll(poke: Pokemon, player: ServerPlayerEntity, shinyBoost: Int? = 0): Float {
        var shinyRate = Cobblemon.config.shinyRate - (shinyBoost ?: 0).toFloat()
        val event = ShinyChanceCalculationEvent(shinyRate, poke)
        CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
            shinyRate = it.calculate(player)
        }
        if (shinyRate == 0f) return 1f
        RadGyms.LOGGER.info("ShinyChance calculation event: $shinyRate")
        return shinyRate
    }

    @Environment(EnvType.CLIENT)
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

            if (poke.species.implemented) {
                if (poke.form.name != poke.species.standardForm.name) {
                    val id = poke.form.name.lowercase()
                    val form = lang("ui.pokedex.info.form.${id}")
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
                    ).styled { it.withFormatting(pokeRarity.formatting) })
            }
        }



        return pokes
    }
}
