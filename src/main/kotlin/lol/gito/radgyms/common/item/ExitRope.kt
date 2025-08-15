/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import lol.gito.radgyms.client.gui.screen.GymLeaveScreen
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.registry.ItemRegistry.EXIT_ROPE
import net.minecraft.client.MinecraftClient
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

class ExitRope : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
                MinecraftClient.getInstance().setScreen(GymLeaveScreen())
            } else {
                user.sendMessage(translatable(EXIT_ROPE.translationKey.plus(".failed")))
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
        tooltip.add(translatable(EXIT_ROPE.translationKey.plus(".tooltip")).formatted(Formatting.GRAY))
    }
}
