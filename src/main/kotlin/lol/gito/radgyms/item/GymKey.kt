package lol.gito.radgyms.item

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gui.GuiHandler
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import lol.gito.radgyms.item.group.ItemGroupManager
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class GymKey : Item(Settings().also { settings ->
    settings
        .group(ItemGroupManager.GYMS_GROUP)
        .rarity(Rarity.UNCOMMON)
        .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
        .stackGenerator(ItemGroupManager::gymTypeItemStacks)
}) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            GuiHandler.openGymKeyScreen(user)
        }

        return super.use(world, user, hand)
    }

    override fun appendTooltip(
        itemStack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val attuned = itemStack.get(DataComponentManager.GYM_TYPE_COMPONENT)
        if (attuned != null) {
            val tooltipText: MutableText = if (ElementalTypes.get(attuned) != null) {
                translatable(
                    ItemRegistry.GYM_KEY.translationKey.plus(".attuned"),
                    translatable(
                        cobblemonResource("type.suffix").toTranslationKey(),
                        translatable(cobblemonResource("type.$attuned").toTranslationKey())
                    )
                )
            } else {
                translatable(
                    ItemRegistry.GYM_KEY.translationKey.plus(".attuned"),
                    translatable(modId("custom_type.$attuned").toTranslationKey())
                )
            }
            tooltip.addLast(tooltipText.formatted(Formatting.GOLD))
        }
    }

    override fun getDefaultStack(): ItemStack {
        val itemStack = ItemStack(this)
        itemStack.set(DataComponentTypes.RARITY, Rarity.UNCOMMON)

        return itemStack
    }
}
