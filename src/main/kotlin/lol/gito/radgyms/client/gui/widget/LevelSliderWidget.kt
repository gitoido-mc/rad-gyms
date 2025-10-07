/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui.widget

import com.cobblemon.mod.common.Cobblemon
import lol.gito.radgyms.api.LevelBoundsResult
import lol.gito.radgyms.api.context.LevelBoundsContext
import lol.gito.radgyms.api.event.LevelBoundsCallback
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Environment(EnvType.CLIENT)
class LevelSliderWidget(x: Int, y: Int, private var minLevel: Int, private var maxLevel: Int, private val onChange: (Int) -> Unit) : SliderWidget(
    x,
    y,
    190,
    20,
    ScreenTexts.EMPTY,
    0.0
) {

    var level: Int = minLevel;

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
        normalizeBounds()
        val clamped = level.coerceIn(minLevel, maxLevel)
        this.level = clamped
        val range = (maxLevel - minLevel).coerceAtLeast(1)
        this.value = (clamped - minLevel).toDouble() / range.toDouble()
    }

    private fun fromSliderValue(): Int {
        normalizeBounds()
        val range = (maxLevel - minLevel).coerceAtLeast(1)
        return floor(value * range + minLevel).toInt().coerceIn(minLevel, maxLevel)
    }

    private fun normalizeBounds() {
        if (maxLevel < minLevel) {
            val t = minLevel
            minLevel = maxLevel
            maxLevel = t
        }

        if(maxLevel == minLevel) {
            maxLevel = minLevel +1
        }
    }
}
