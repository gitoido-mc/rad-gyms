package lol.gito.radgyms.gui.widget

import com.cobblemon.mod.common.api.text.add
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.cobblemonResource
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import lol.gito.radgyms.cache.CacheHandler
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity

class CacheInfoHolder(
    val type: ElementalType,
    val rarity: Rarity,
    horizontalSizing: Sizing,
    verticalSizing: Sizing,
    algorithm: Algorithm = Algorithm.HORIZONTAL,
) : FlowLayout(horizontalSizing, verticalSizing, algorithm) {
    fun build(): CacheInfoHolder {
        val typeSprite = ElementalTypeSprite(type, 1, 1, 20, 50, 20, 50)
        typeSprite.sizing(Sizing.fixed(20), Sizing.fixed(50))

        child(typeSprite)

        val info = Containers.verticalFlow(Sizing.expand(), Sizing.expand())
        info.padding(Insets.horizontal(5))
        info.child(
            Components.label(
                translatable(
                    cobblemonResource("type.suffix").toTranslationKey(),
                    translatable(cobblemonResource("type.${type.name}").toTranslationKey())
                )
            )
        )

        val pokeList = Containers.horizontalFlow(Sizing.expand(), Sizing.expand())
        pokeList.gap(2)

        CacheHandler.getPokeNames(type, rarity).forEach { name: MutableText ->
            name.add(",")
            val label = Components.label(name)
            label.sizing(Sizing.content())
            pokeList.child(label)
        }

        info.child(pokeList)
        child(info)

        return this
    }
}
