/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.api.text.text
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

fun MutableComponent.rainbow(): MutableComponent {
    val intermediate: String? = this.string
    val colors: List<ChatFormatting> = listOf(
        ChatFormatting.RED,
        ChatFormatting.GOLD,
        ChatFormatting.YELLOW,
        ChatFormatting.GREEN,
        ChatFormatting.AQUA,
        ChatFormatting.BLUE,
        ChatFormatting.DARK_PURPLE
    )
    val result = Component.empty()
    var colorIndex = 0
    intermediate?.forEach { c ->
        result.append(c.toString().text().withStyle(colors[colorIndex]))
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