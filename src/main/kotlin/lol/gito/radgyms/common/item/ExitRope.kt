/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.registry.ItemRegistry.EXIT_ROPE
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ExitRope : Item(Settings()) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            return TypedActionResult.pass(player.getStackInHand(hand))
        }

        if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            ServerPlayNetworking.send(
                player as ServerPlayerEntity, OpenGymLeaveScreenS2C(OpenGymLeaveScreenS2C.PACKET_ID)
            )
        } else {
            player.sendMessage(translatable(EXIT_ROPE.translationKey.plus(".failed")))
        }

        return super.use(world, player, hand)
    }

    override fun getDefaultStack(): ItemStack {
        val itemStack = ItemStack(this)
        itemStack.set(DataComponentTypes.RARITY, Rarity.COMMON)

        return itemStack
    }

    override fun appendTooltip(
        stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType
    ) {
        tooltip.add(translatable(EXIT_ROPE.translationKey.plus(".tooltip")).formatted(Formatting.GRAY))
    }
}
