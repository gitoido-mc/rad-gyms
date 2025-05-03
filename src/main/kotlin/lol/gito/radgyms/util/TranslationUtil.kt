package lol.gito.radgyms.util

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.RadGyms.modId
import net.minecraft.text.Text.translatable

object TranslationUtil {
    fun attuneType(cacheType: String?) = if (cacheType?.let { ElementalTypes.get(it) } != null) {
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
