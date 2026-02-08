/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.gui.widget

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.util.math.floor
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractSliderButton
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class LevelSliderWidget(
    x: Int,
    y: Int,
    private val initialLevel: Int = RadGyms.CONFIG.minLevel!!,
    private val minLevel: Int,
    private val maxLevel: Int,
    private val onChange: (Int) -> Unit
) : AbstractSliderButton(x, y, 190, 20, CommonComponents.EMPTY, 0.0) {
    var level: Int = this.initialLevel

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        updateLevel(this.level)
        updateMessage()
        super.renderWidget(context, mouseX, mouseY, delta)
    }

    override fun updateMessage() {
        this.message = Component.literal(level.toString())
    }

    override fun applyValue() {
        level = fromSliderValue()
        onChange(level)
    }

    fun updateLevel(level: Int) {
        this.level = level
        this.value = level
            .toDouble()
            .minus(this.minLevel)
            .div(maxLevel.minus(minLevel))
    }

    private fun fromSliderValue(): Int = (value - 0.0)
        .times(maxLevel - minLevel)
        .div(1.0 - 0.0)
        .plus(minLevel)
        .floor()
        .toInt()
}