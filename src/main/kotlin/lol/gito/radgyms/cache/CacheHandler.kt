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
import lol.gito.radgyms.gym.SpeciesManager.SPECIES_BY_RARITY
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity
import kotlin.random.Random

object CacheHandler {
    private fun Float.checkRate(): Boolean =
        if (this >= 1) (Random.Default.nextFloat() < 1 / this) else Random.Default.nextFloat() < this

    @Environment(EnvType.SERVER)
    fun getPoke(type: ElementalType, rarity: Rarity, player: ServerPlayerEntity): Pokemon {
        val cache = SPECIES_BY_RARITY[type.name]!!.forRarity(rarity)

        val pokeProps = PokemonProperties.parse(cache.shuffle().first())
        val poke = pokeProps.create()

        poke.shiny = shinyRoll(poke, player).checkRate()
        val hasShinyCharm = player.inventory.contains {
            it.registryEntry.idAsString == "unimplemented_items:shiny_charm" // Make it a tag?
        }

        if (!poke.shiny && hasShinyCharm) {
            poke.shiny = shinyRoll(poke, player).checkRate()
        }

        poke.updateAspects()
        poke.updateForm()

        return poke.initialize()
    }

    @Environment(EnvType.SERVER)
    private fun shinyRoll(poke: Pokemon, player: ServerPlayerEntity): Float {
        var shinyRate = Cobblemon.config.shinyRate
        val event = ShinyChanceCalculationEvent(shinyRate, poke)
        CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
            shinyRate = it.calculate(player)
        }
        return shinyRate
    }

    @Environment(EnvType.CLIENT)
    fun getPokeNames(type: ElementalType, rarity: Rarity?): List<MutableText> {
        val pokes = mutableListOf<MutableText>()

        (1..5).forEach { _ ->
            val pokeRarity = when (rarity) {
                null -> Rarity.entries.random()
                else -> rarity
            }

            val pokeProps = PokemonProperties.parse(
                SPECIES_BY_RARITY[type.name]!!.forRarity(pokeRarity).shuffle().first()
            )
            val poke = pokeProps.create()

            poke.updateAspects()
            poke.updateForm()


            if (poke.species.implemented) {
                if (poke.form.name != poke.species.standardForm.name) {
                    val form = lang(
                        "ui.pokedex.info.form.${poke.form.formOnlyShowdownId().lowercase()}"
                    )
                    val pokeName = lang("species.${poke.species.showdownId().lowercase()}.name").add(" ").add("(")
                        .add(form.string).add(")").styled { it.withFormatting(pokeRarity.formatting) }

                    pokes.add(
                        pokeName
                    )

                    return@forEach
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
