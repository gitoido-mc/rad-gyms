/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.text.MutableText
import net.minecraft.text.Text.translatable

object TranslationUtil {
    fun attuneType(cacheType: String?): MutableText = if (cacheType?.let { ElementalTypes.get(it) } != null) {
        translatable(
            modId("item.component.gym_type").toTranslationKey(),
            translatable(
                cobblemonResource("type.suffix").toTranslationKey(),
                translatable(cobblemonResource("type.$cacheType").toTranslationKey())
            )
        )
    } else {
        translatable(
            modId("item.component.gym_type").toTranslationKey(),
            translatable(
                cobblemonResource("type.suffix").toTranslationKey(),
                modId("item.component.type.$cacheType").toTranslationKey()
            )
        )
    }
}
