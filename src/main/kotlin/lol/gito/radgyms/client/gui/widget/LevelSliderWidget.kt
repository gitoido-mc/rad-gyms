/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui.widget

import com.cobblemon.mod.common.Cobblemon
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import kotlin.math.floor

@Environment(EnvType.CLIENT)
class LevelSliderWidget(x: Int, y: Int, private val onChange: (Int) -> Unit) : SliderWidget(
    x,
    y,
    190,
    20,
    ScreenTexts.EMPTY,
    0.0
) {
    private val minLevel: Int = 10

    var level: Int = 10

    override fun updateMessage() {
        this.message = Text.literal(level.toString())
    }

    override fun applyValue() {
        level = fromSliderValue()
        onChange(level)
    }

    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        updateMessage()
        super.renderWidget(context, mouseX, mouseY, delta)
    }

    fun updateLevel(level: Int) {
        this.level = level
        this.value = level.toDouble().minus(minLevel).div(Cobblemon.config.maxPokemonLevel.minus(minLevel))
    }

    private fun fromSliderValue(): Int = floor(
        (value - 0.0).times(Cobblemon.config.maxPokemonLevel - minLevel).div(1.0 - 0.0).plus(minLevel)
    ).toInt()
}