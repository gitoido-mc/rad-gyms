package lol.gito.radgyms.item

import lol.gito.radgyms.gui.GuiHandler
import lol.gito.radgyms.item.ItemRegistry.Companion.EXIT_ROPE
import lol.gito.radgyms.world.DimensionManager
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ExitRope : Item(Settings().group(ItemGroupManager.GYMS_GROUP)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            if (world.registryKey == DimensionManager.RADGYMS_LEVEL_KEY) {
                GuiHandler.openGymLeaveScreen(user)
            } else {
                user.sendMessage(translatable( EXIT_ROPE.translationKey.plus(".failed")))
            }
        }

        return super.use(world, user, hand)
    }

    override fun getDefaultStack(): ItemStack {
        val itemStack = ItemStack(this)
        itemStack.set(DataComponentTypes.RARITY, Rarity.COMMON)

        return itemStack
    }

    override fun appendTooltip(
        stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType
    ) {
        tooltip.add(translatable( EXIT_ROPE.translationKey.plus(".tooltip")).formatted(Formatting.GRAY))
    }
}