package lol.gito.radgyms.item

import lol.gito.radgyms.gui.GuiHandler
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class GymKey(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            GuiHandler.openGymKeyScreen(user)
        }

        return super.use(world, user, hand)
    }
}