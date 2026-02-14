package lol.gito.radgyms.common.item

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.level.Level

class GymRewardBag : BundleItem(
    Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
) {
    companion object {
        const val DROP_SOUND_VOLUME = 0.8f
        const val DROP_SOUND_PITCH = 0.4f
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(interactionHand)
        if (dropContents(itemStack, player)) {
            this.playDropContentsSound(player)
            player.awardStat(Stats.ITEM_USED.get(this))
            return InteractionResultHolder.sidedSuccess<ItemStack>(itemStack, level.isClientSide())
        } else {
            return InteractionResultHolder.fail<ItemStack>(itemStack)
        }
    }

    override fun overrideOtherStackedOnMe(
        itemStack: ItemStack,
        itemStack2: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player,
        slotAccess: SlotAccess
    ): Boolean = false

    override fun overrideStackedOnOther(
        itemStack: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player
    ): Boolean = false

    override fun isBarVisible(itemStack: ItemStack): Boolean = false

    private fun dropContents(stack: ItemStack, player: Player): Boolean {
        val bundleContents = stack.get<BundleContents>(DataComponents.BUNDLE_CONTENTS)
        if (bundleContents != null && !bundleContents.isEmpty) {
            if (player is ServerPlayer) {
                bundleContents.itemsCopy().forEach { player.drop(it, true) }
                stack.shrink(1)
            }

            return true
        } else {
            return false
        }
    }

    private fun playDropContentsSound(entity: Entity) {
        entity.playSound(
            SoundEvents.BUNDLE_DROP_CONTENTS,
            DROP_SOUND_VOLUME,
            DROP_SOUND_VOLUME + entity.level().getRandom().nextFloat() * DROP_SOUND_PITCH
        )
    }
}
