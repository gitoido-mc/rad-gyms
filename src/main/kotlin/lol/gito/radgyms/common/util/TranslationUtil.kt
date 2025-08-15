/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting

object TranslationUtil {
    fun buildPrefixedSuffixedTypeText(elementalType: ElementalType? = null): MutableText =
        buildPrefixedSuffixedTypeText(elementalType?.name?.lowercase())

    fun buildPrefixedSuffixedTypeText(elementalType: String? = null): MutableText = translatable(
        modId("item.component.gym_type").toTranslationKey(),
        buildSuffixedTypeText(elementalType)
    )

    fun buildSuffixedTypeText(elementalType: ElementalType? = null): MutableText =
        buildSuffixedTypeText(elementalType?.name?.lowercase())

    fun buildSuffixedTypeText(elementalType: String? = null): MutableText = translatable(
        cobblemonResource("type.suffix").toTranslationKey(),
        buildTypeText(elementalType)
    )

    fun buildTypeText(elementalType: ElementalType? = null): MutableText =
        buildTypeText(elementalType?.name?.lowercase())

    fun buildTypeText(elementalType: String? = null): MutableText = when {
        (elementalType == null) -> translatable(modId("item.component.type.chaos").toTranslationKey()).styled {
            it.withFormatting(Formatting.OBFUSCATED).withFormatting(Formatting.DARK_GRAY)
        }

        (ElementalTypes.get(elementalType) != null) -> translatable(cobblemonResource("type.$elementalType").toTranslationKey()).styled {
            it.withFormatting(Formatting.DARK_PURPLE)
        }

        else -> translatable(modId("item.component.type.$elementalType").toTranslationKey()).styled {
            it.withFormatting(Formatting.GOLD)
        }
    }
}
