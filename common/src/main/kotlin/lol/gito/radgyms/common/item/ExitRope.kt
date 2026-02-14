/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.common.EXIT_ROPE_COOLDOWN
import lol.gito.radgyms.common.net.server.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.extension.displayClientMessage
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ExitRope : CobblemonItem(Properties()) {
    override fun use(
        level: Level,
        player: Player,
        hand: InteractionHand,
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand))

        when (level.dimension()) {
            RadGymsDimensions.RADGYMS_LEVEL_KEY -> OpenGymLeaveScreenS2C().sendToPlayer(player as ServerPlayer)

            else -> player.displayClientMessage(translatable(EXIT_ROPE.descriptionId.plus(".failed")))
        }


        player.cooldowns.addCooldown(this, EXIT_ROPE_COOLDOWN)
        return super.use(level, player, hand)
    }



    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltip.add(translatable(EXIT_ROPE.descriptionId.plus(".tooltip")).withStyle(ChatFormatting.GRAY))
    }

    override fun getDefaultInstance(): ItemStack = ItemStack(this).apply {
        set(DataComponents.RARITY, Rarity.COMMON)
    }
}
