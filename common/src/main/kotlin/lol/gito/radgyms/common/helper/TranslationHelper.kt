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
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

fun tlc(key: String, vararg args: Any?): MutableComponent = translatable(
    cobblemonResource(key).toLanguageKey(),
    *args
)

fun tl(key: String, vararg args: Any?): MutableComponent = translatable(
    modId(key).toLanguageKey(),
    *args
)
fun tl(prefix: String, key: String, vararg args: Any?): MutableComponent = translatable(
    modId(key).toLanguageKey(prefix),
    *args
)

fun tlk(key: String): String = modId(key).toLanguageKey()

fun tlk(prefix: String, key: ResourceLocation): String = key.toLanguageKey(prefix)
fun tlk(prefix: String, key: String): String = modId(key).toLanguageKey(prefix)

object ElementalTypeTranslationHelper {
    @Suppress("unused")
    fun buildPrefixedSuffixedTypeText(elementalType: ElementalType? = null): Component =
        buildPrefixedSuffixedTypeText(elementalType?.showdownId)

    fun buildPrefixedSuffixedTypeText(elementalType: String? = null): Component = tl(
        "item.component.gym_type",
        buildSuffixedTypeText(elementalType)
    )

    @Suppress("unused")
    fun buildSuffixedTypeText(elementalType: ElementalType? = null): Component =
        buildSuffixedTypeText(elementalType?.showdownId)

    fun buildSuffixedTypeText(elementalType: String? = null): Component = tlc(
        "type.suffix",
        buildTypeText(elementalType)
    )

    @Suppress("unused")
    fun buildTypeText(elementalType: ElementalType? = null): Component =
        buildTypeText(elementalType?.showdownId)

    fun buildTypeText(elementalType: String? = null): Component = when {
        (elementalType == null) -> tl("item.component.type.chaos").withStyle {
            it.applyFormat(ChatFormatting.OBFUSCATED).applyFormat(ChatFormatting.DARK_GRAY)
        }

        (ElementalTypes.get(elementalType) != null) -> tlc("type.$elementalType").withStyle {
            it.applyFormat(ChatFormatting.DARK_PURPLE)
        }

        (elementalType == "chaos") -> tl("item.component.type.chaos").withStyle {
            it.applyFormat(ChatFormatting.DARK_GRAY).applyFormat(ChatFormatting.OBFUSCATED)
        }

        else -> tl("item.component.type.$elementalType").withStyle {
            it.applyFormat(ChatFormatting.GOLD)
        }
    }
}
