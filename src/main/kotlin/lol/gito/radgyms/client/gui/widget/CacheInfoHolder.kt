/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui.widget

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.cobblemonResource
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.cache.CacheHandler
import lol.gito.radgyms.client.gui.CacheAttuneScreen
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_SELECT
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
    private val shinyBoost: Int,
    private val parentScreen: CacheAttuneScreen
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

        val select = Components.button(translatable(Identifier.of("gui:proceed").toTranslationKey())) {
            CHANNEL.clientHandle().send(
                NetworkStackHandler.CacheOpen(
                    type = type.name,
                    rarity = rarity,
                    shinyBoost = shinyBoost,
                )
            )
            parentScreen.close()
        }.apply {
            this.id(ID_SELECT)
            this.margins(Insets.right(5))
        }

        child(info)
        child(select)

        return this
    }
}
