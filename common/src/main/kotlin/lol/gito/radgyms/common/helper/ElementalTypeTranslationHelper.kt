/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.helper

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable

@Suppress("unused")
object ElementalTypeTranslationHelper {
    fun buildPrefixedSuffixedTypeText(elementalType: ElementalType? = null): Component =
        buildPrefixedSuffixedTypeText(elementalType?.showdownId)

    fun buildPrefixedSuffixedTypeText(elementalType: String? = null): Component = translatable(
        modId("item.component.gym_type").toLanguageKey(),
        buildSuffixedTypeText(elementalType)
    )

    fun buildSuffixedTypeText(elementalType: ElementalType? = null): Component =
        buildSuffixedTypeText(elementalType?.showdownId)

    fun buildSuffixedTypeText(elementalType: String? = null): Component = translatable(
        cobblemonResource("type.suffix").toLanguageKey(),
        buildTypeText(elementalType)
    )

    fun buildTypeText(elementalType: ElementalType? = null): Component =
        buildTypeText(elementalType?.showdownId)

    fun buildTypeText(elementalType: String? = null): Component = when {
        (elementalType == null) -> {
            translatable(modId("item.component.type.chaos").toLanguageKey())
                .withStyle {
                    it.applyFormat(ChatFormatting.OBFUSCATED).applyFormat(ChatFormatting.DARK_GRAY)
                }
        }

        (ElementalTypes.get(elementalType) != null) -> {
            translatable(cobblemonResource("type.$elementalType").toLanguageKey())
                .withStyle {
                    it.applyFormat(ChatFormatting.DARK_PURPLE)
                }
        }

        (elementalType == "chaos") -> {
            translatable(modId("item.component.type.chaos").toLanguageKey())
                .withStyle {
                    it.applyFormat(ChatFormatting.DARK_GRAY).applyFormat(ChatFormatting.OBFUSCATED)
                }
        }

        else -> {
            translatable(modId("item.component.type.$elementalType").toLanguageKey()).withStyle {
                it.applyFormat(ChatFormatting.GOLD)
            }
        }
    }
}
