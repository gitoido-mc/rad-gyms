package lol.gito.radgyms.gui.widget

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.runOnServer
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.cache.CacheHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text.literal
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

class CacheInfoHolder(
    val player: PlayerEntity,
    val type: ElementalType,
    val rarity: Rarity,
    private val shinyBoost: Int,
    horizontalSizing: Sizing,
    verticalSizing: Sizing,
    algorithm: Algorithm = Algorithm.HORIZONTAL,
) : FlowLayout(horizontalSizing, verticalSizing, algorithm) {
    fun build(): CacheInfoHolder {
        val typeSprite = ElementalTypeSprite(type, 1, 1, 16, 16, 16, 16)
        typeSprite.sizing(Sizing.content(), Sizing.content())


        child(typeSprite)

        val info = Containers.verticalFlow(Sizing.expand(), Sizing.content())

        info.padding(Insets.horizontal(5))
        info.child(
            Components.label(
                translatable(
                    cobblemonResource("type.suffix").toTranslationKey(),
                    translatable(cobblemonResource("type.${type.name}").toTranslationKey())
                )
            )
        )
        val pokeList = Containers.ltrTextFlow(Sizing.expand(), Sizing.content())
        pokeList.gap(6)
        pokeList.padding(Insets.vertical(5))

        CacheHandler.getPokeNames(type, rarity).forEach { name: MutableText ->
            val label = Components.label(name)
            label.sizing(Sizing.content())
            pokeList.child(label)
        }

        info.child(pokeList)

        child(info)
        child(
            Components.button(
                translatable(Identifier.of("gui:proceed").toTranslationKey())
            ) {
                runOnServer {
                    val poke: Pokemon = CacheHandler.getPoke(type, rarity, player.uuid, shinyBoost, addToParty = true)
                    player.sendMessage(literal("Rolled $rarity $type ${poke.species.name} shiny: ${poke.shiny}"))
                    RadGyms.LOGGER.info("shiny boost: $shinyBoost")
                    player.mainHandStack.decrementUnlessCreative(1, player)
                }

            }.apply {
                this.margins(Insets.right(5))
            }
        )

        return this
    }
}
