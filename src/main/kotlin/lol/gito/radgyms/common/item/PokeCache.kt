/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.network.payload.CacheOpenC2S
import lol.gito.radgyms.common.registry.DataComponentRegistry.CACHE_SHINY_BOOST_COMPONENT
import lol.gito.radgyms.common.registry.DataComponentRegistry.GYM_TYPE_COMPONENT
import lol.gito.radgyms.common.util.TranslationUtil.buildPrefixedSuffixedTypeText
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.component.DataComponentTypes.RARITY
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

const val RG_CACHE_BLOCK_BOOST = 9

class CommonPokeCache : PokeCache(Rarity.COMMON)
class UncommonPokeCache : PokeCache(Rarity.UNCOMMON)
class RarePokeCache : PokeCache(Rarity.RARE)
class EpicPokeCache : PokeCache(Rarity.EPIC)

open class PokeCache(private val rarity: Rarity) : Item(Settings().rarity(rarity)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (hand != Hand.MAIN_HAND) return TypedActionResult.fail(user.getStackInHand(hand))

        val stack = user.getStackInHand(hand)
        val offhand = user.offHandStack
        val boost = stack.getOrDefault(CACHE_SHINY_BOOST_COMPONENT, 0)
        val type = stack.getOrDefault(GYM_TYPE_COMPONENT, null)
        if (offhand.isOf(Items.LAPIS_LAZULI) && !world.isClient && boost < Cobblemon.config.shinyRate) {
            stack.set(CACHE_SHINY_BOOST_COMPONENT, boost.plus(CONFIG.lapisBoostAmount!!))
        }
        if (offhand.isOf(Items.LAPIS_BLOCK) && !world.isClient && boost < Cobblemon.config.shinyRate) {
            val newBoost = when (boost.plus(RG_CACHE_BLOCK_BOOST) > Cobblemon.config.shinyRate) {
                true -> Cobblemon.config.shinyRate.toInt()
                false -> boost.plus(RG_CACHE_BLOCK_BOOST * CONFIG.lapisBoostAmount!!)
                    .coerceAtMost(Cobblemon.config.shinyRate.toInt() - 1)
            }
            stack.set(CACHE_SHINY_BOOST_COMPONENT, newBoost)
        }

        if (offhand.isOf(Items.LAPIS_LAZULI) || offhand.isOf(Items.LAPIS_BLOCK)) {
            offhand.decrementUnlessCreative(1, user)
            return TypedActionResult.success(user.getStackInHand(hand), true)
        }

        if (world.isClient) {
            val cacheType = when (type) {
                "chaos", null -> ElementalTypes.all().random().name.lowercase()
                else -> type
            }
            ClientPlayNetworking.send(
                CacheOpenC2S(
                    type = cacheType,
                    rarity = rarity,
                    shinyBoost = boost,
                )
            )
        }

        return TypedActionResult.success(user.getStackInHand(hand), true)
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val shinyBoost = stack.get(CACHE_SHINY_BOOST_COMPONENT)
        val cacheType = stack.get(GYM_TYPE_COMPONENT)
        if (shinyBoost != null && shinyBoost > 0) {
            val tooltipText = translatable(
                modId("item.component.shiny_boost").toTranslationKey(),
                "1/${(Cobblemon.config.shinyRate.toInt() - shinyBoost).coerceAtLeast(1)}"
            )

            tooltip.addLast(tooltipText.formatted(Formatting.GOLD).formatted(Formatting.BOLD))
        }

        val tooltipText: MutableText = buildPrefixedSuffixedTypeText(cacheType)
        tooltip.addLast(tooltipText)
    }

    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.set(RARITY, this.rarity)
        stack.set(CACHE_SHINY_BOOST_COMPONENT, 0)

        return stack
    }

    override fun hasGlint(stack: ItemStack): Boolean {
        return stack.getOrDefault(CACHE_SHINY_BOOST_COMPONENT, 0) > 0
    }
}
