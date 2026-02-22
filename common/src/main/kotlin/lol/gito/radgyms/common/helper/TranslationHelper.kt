/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.helper

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

private typealias RL = ResourceLocation
private typealias MC = MutableComponent
private typealias C = Component

fun tl(key: String, vararg args: Any?): MC = translatable(RL.parse(key).toLanguageKey(), *args)
fun tl(key: RL, vararg args: Any?): MC = translatable(key.toLanguageKey(), *args)

fun tl(prefix: String, key: String, vararg args: Any?): MC = translatable(RL.parse(key).toLanguageKey(prefix), *args)
fun tl(prefix: String, key: RL, vararg args: Any?): MC = translatable(key.toLanguageKey(prefix), *args)

fun tlr(key: String, vararg args: Any?): MC = translatable(key, *args)

fun tlk(key: String): String = RL.parse(key).toLanguageKey()

fun tlk(prefix: String, key: String): String = RL.parse(key).toLanguageKey(prefix)
fun tlk(prefix: String, key: RL): String = key.toLanguageKey(prefix)

fun tlc(key: String, vararg args: Any?): MC = translatable(cobblemonResource(key).toLanguageKey(), *args)

object ElementalTypeTranslationHelper {
    @Suppress("unused")
    fun buildPrefixedSuffixedTypeText(elementalType: ElementalType? = null): C =
        buildPrefixedSuffixedTypeText(elementalType?.showdownId)

    fun buildPrefixedSuffixedTypeText(elementalType: String? = null): C = tl(
        "item.component.gym_type",
        buildSuffixedTypeText(elementalType),
    )

    @Suppress("unused")
    fun buildSuffixedTypeText(elementalType: ElementalType? = null): C =
        buildSuffixedTypeText(elementalType?.showdownId)

    fun buildSuffixedTypeText(elementalType: String? = null): C = tlc(
        "type.suffix",
        buildTypeText(elementalType),
    )

    @Suppress("unused")
    fun buildTypeText(elementalType: ElementalType? = null): C = buildTypeText(elementalType?.showdownId)

    fun buildTypeText(elementalType: String? = null): C = when {
        (elementalType == null) ->
            tl("item.component.type.chaos").withStyle {
                it.applyFormat(ChatFormatting.OBFUSCATED).applyFormat(ChatFormatting.DARK_GRAY)
            }

        (ElementalTypes.get(elementalType) != null) ->
            tlc("type.$elementalType").withStyle {
                it.applyFormat(ChatFormatting.DARK_PURPLE)
            }

        (elementalType == "chaos") ->
            tl("item.component.type.chaos").withStyle {
                it.applyFormat(ChatFormatting.DARK_GRAY).applyFormat(ChatFormatting.OBFUSCATED)
            }

        else ->
            tl("item.component.type.$elementalType").withStyle {
                it.applyFormat(ChatFormatting.GOLD)
            }
    }
}
