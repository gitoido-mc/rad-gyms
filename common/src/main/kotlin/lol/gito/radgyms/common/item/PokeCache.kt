/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms.config
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.cache.CacheHandler
import lol.gito.radgyms.common.helper.ElementalTypeTranslationHelper.buildPrefixedSuffixedTypeText
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsDataComponents.RG_CACHE_SHINY_BOOST_COMPONENT
import lol.gito.radgyms.common.registry.RadGymsDataComponents.RG_GYM_TYPE_COMPONENT
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents.RARITY
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.InteractionResultHolder.fail
import net.minecraft.world.InteractionResultHolder.sidedSuccess
import net.minecraft.world.InteractionResultHolder.success
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items.LAPIS_BLOCK
import net.minecraft.world.item.Items.LAPIS_LAZULI
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

const val RG_CACHE_BLOCK_BOOST = 9

open class PokeCache(private val rarity: Rarity) : CobblemonItem(Properties().rarity(rarity)) {
    override fun use(level: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> = when {
        level.isClientSide -> sidedSuccess(user.getItemInHand(hand), true)
        (hand != InteractionHand.MAIN_HAND) -> fail(user.getItemInHand(hand))
        (user.offhandItem.item in listOf(LAPIS_LAZULI, LAPIS_BLOCK)) -> sidedSuccess(user.getItemInHand(hand), true)
        else -> {
            val stack = user.getItemInHand(hand)
            val rarity = stack.getOrDefault(RARITY, Rarity.COMMON)
            val boost = calculateCacheBoost(stack, user.offhandItem, user)
            val type =
                when (stack.getOrDefault(RG_GYM_TYPE_COMPONENT, ElementalTypes.all().random().showdownId)) {
                    "chaos" -> ElementalTypes.all().random().showdownId
                    else -> stack.getOrDefault(RG_GYM_TYPE_COMPONENT, ElementalTypes.all().random().showdownId)
                }
            val poke: Pokemon = CacheHandler.getPoke(type, rarity, user as ServerPlayer, boost)
            CACHE_ROLL_POKE.emit(GymEvents.CacheRollPokeEvent(user, poke, type, rarity, boost))

            success(user.getItemInHand(hand))
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        type: TooltipFlag,
    ) = with(stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0)) {
        if (this > 0) {
            val tooltipText = tl(
                "item.component.shiny_boost",
                "1/${(Cobblemon.config.shinyRate.toInt() - this).coerceAtLeast(1)}",
            )

            tooltip.add(tooltipText.withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD))
        }

        val tooltipText: Component = buildPrefixedSuffixedTypeText(stack.get(RG_GYM_TYPE_COMPONENT))
        tooltip.add(tooltipText)

        return@with
    }

    override fun getDefaultInstance(): ItemStack = super.defaultInstance.also { stack ->
        stack.set(RARITY, this.rarity)
        stack.set(RG_CACHE_SHINY_BOOST_COMPONENT, 0)
    }

    override fun isFoil(stack: ItemStack): Boolean = stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0) > 0

    private fun calculateCacheBoost(stack: ItemStack, offhand: ItemStack, user: Player): Int =
        with(stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0)) {
            if (equals(Cobblemon.config.shinyRate)) return@with this

            return@with when (offhand.item) {
                LAPIS_LAZULI -> this.plus(config.lapisBoostAmount!!).also {
                    stack.set(RG_CACHE_SHINY_BOOST_COMPONENT, it)
                    offhand.consume(1, user)
                }

                LAPIS_BLOCK -> this.plus(RG_CACHE_BLOCK_BOOST * config.lapisBoostAmount!!)
                    .coerceAtMost(Cobblemon.config.shinyRate.toInt().dec())
                    .also {
                        stack.set(RG_CACHE_SHINY_BOOST_COMPONENT, it)
                        offhand.consume(1, user)
                    }

                else -> this
            }
        }
}

class CommonPokeCache : PokeCache(Rarity.COMMON)

class UncommonPokeCache : PokeCache(Rarity.UNCOMMON)

class RarePokeCache : PokeCache(Rarity.RARE)

class EpicPokeCache : PokeCache(Rarity.EPIC)
