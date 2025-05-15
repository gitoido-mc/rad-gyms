package lol.gito.radgyms.gui.widget

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.cobblemonResource
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.cache.CacheHandler
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import oshi.util.tuples.Quartet

class CacheInfoHolder(
    val player: PlayerEntity,
    val type: ElementalType,
    val rarity: Rarity,
    private val shinyBoost: Int
) : FlowLayout(Sizing.expand(), Sizing.content(), Algorithm.HORIZONTAL) {
    fun build(): CacheInfoHolder {
        val typeSize = Quartet(1, 1, 16, 16)

        val typeSprite = ElementalTypeSprite(
            type,
            typeSize.a,
            typeSize.b,
            typeSize.c,
            typeSize.d,
            typeSize.c,
            typeSize.d
        )
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
                CHANNEL.clientHandle().send(
                    NetworkStackHandler.CacheOpen(
                        type = type.name,
                        rarity = rarity,
                        shinyBoost = shinyBoost,
                    )
                )
            }.apply {
                this.margins(Insets.right(5))
            }
        )

        return this
    }
}
