package lol.gito.radgyms.item

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import lol.gito.radgyms.item.group.ItemGroupManager
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity

class CommonPokeCache: PokeCache(Rarity.COMMON)
class UncommonPokeCache: PokeCache(Rarity.UNCOMMON)
class RarePokeCache: PokeCache(Rarity.RARE)
class EpicPokeCache: PokeCache(Rarity.EPIC)

open class PokeCache(private val rarity: Rarity) : Item(
    Settings().group(ItemGroupManager.GYMS_GROUP).rarity(rarity)
) {
    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.set(DataComponentTypes.RARITY, this.rarity)

        return stack
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val shinyBoost = stack.get(DataComponentManager.CACHE_SHINY_BOOST_COMPONENT)
        val cacheType = stack.get(DataComponentManager.GYM_TYPE_COMPONENT)
        if (shinyBoost != null) {
            val tooltipText = translatable(
                modId("cache.component.shiny_boost").toTranslationKey(),
                shinyBoost.toString()
            )

            tooltip.addLast(tooltipText.formatted(Formatting.DARK_PURPLE).formatted(Formatting.BOLD))
        }

        if (cacheType != null) {
            val tooltipText: MutableText = if (ElementalTypes.get(cacheType) != null) {
                translatable(
                    ItemRegistry.GYM_KEY.translationKey.plus(".attuned"),
                    translatable(
                        cobblemonResource("type.suffix").toTranslationKey(),
                        translatable(cobblemonResource("type.$cacheType").toTranslationKey())
                    )
                )
            } else {
                translatable(
                    ItemRegistry.GYM_KEY.translationKey.plus(".attuned"),
                    translatable(modId("custom_type.$cacheType").toTranslationKey())
                )
            }
            tooltip.addLast(tooltipText.formatted(Formatting.GOLD))
        }
    }
}
