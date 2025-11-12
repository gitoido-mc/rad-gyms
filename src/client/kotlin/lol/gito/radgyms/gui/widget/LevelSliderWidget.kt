/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.gui.widget

import lol.gito.radgyms.RadGyms
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import kotlin.math.floor

@Environment(EnvType.CLIENT)
class LevelSliderWidget(
    x: Int,
    y: Int,
    private val initialLevel: Int = RadGyms.CONFIG.minLevel!!,
    private val minLevel: Int,
    private val maxLevel: Int,
    private val onChange: (Int) -> Unit
) : SliderWidget(
    x,
    y,
    190,
    20,
    ScreenTexts.EMPTY,
    0.0
) {
    var level: Int = this.initialLevel

    override fun updateMessage() {
        this.message = Text.literal(level.toString())
    }

    override fun applyValue() {
        level = fromSliderValue()
        onChange(level)
    }

    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        updateLevel(this.level)
        updateMessage()
        super.renderWidget(context, mouseX, mouseY, delta)
    }

    fun updateLevel(level: Int) {
        this.level = level
        this.value = level.toDouble().minus(this.minLevel).div(maxLevel.minus(minLevel))
    }

    private fun fromSliderValue(): Int = floor(
        (value - 0.0).times(maxLevel - minLevel).div(1.0 - 0.0).plus(minLevel)
    ).toInt()
}