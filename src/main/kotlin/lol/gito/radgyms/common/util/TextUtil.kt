/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.api.text.text
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun MutableText.rainbow(): MutableText {
    val intermediate: String? = this.string
    val colors: List<Formatting> = listOf(
        Formatting.RED,
        Formatting.GOLD,
        Formatting.YELLOW,
        Formatting.GREEN,
        Formatting.AQUA,
        Formatting.BLUE,
        Formatting.DARK_PURPLE
    )
    val result = Text.empty()
    var colorIndex = 0
    intermediate?.forEach { c ->
        result.append(c.toString().text().formatted(colors[colorIndex]))
        when (colorIndex) {
            colors.count() - 1 -> {
                colorIndex = 0
            }

            else -> {
                colorIndex++
            }
        }
    }

    return result
}