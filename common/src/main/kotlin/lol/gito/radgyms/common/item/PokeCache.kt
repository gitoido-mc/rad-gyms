/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms.defaultElementalTypes
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.cache.CacheHandler
import lol.gito.radgyms.common.config.RadGymsConfigs
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
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

open class PokeCache(private val rarity: Rarity) : CobblemonItem(Properties().rarity(rarity)) {
    override fun use(level: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> = when {
        level.isClientSide -> sidedSuccess(user.getItemInHand(hand), true)

        (hand != InteractionHand.MAIN_HAND) -> fail(user.getItemInHand(hand))

        else -> {
            val stack = user.getItemInHand(hand)

            if (RadGymsConfigs.server.boosterMap.containsKey(user.offhandItem.item)) {
                val boosterAmount = RadGymsConfigs.server.boosterMap[user.offhandItem.item]!!
                val stackBoost = stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0)
                stack.set(
                    RG_CACHE_SHINY_BOOST_COMPONENT,
                    stackBoost.plus(boosterAmount).coerceAtMost(Cobblemon.config.shinyRate.toInt().dec()),
                )
                user.offhandItem.shrink(1)
                return sidedSuccess(user.getItemInHand(hand), true)
            }

            val rarity = stack.getOrDefault(RARITY, Rarity.COMMON)
            val boost = stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0)
            val type = when (stack.getOrDefault(RG_GYM_TYPE_COMPONENT, defaultElementalTypes.random())) {
                "chaos" -> defaultElementalTypes.random()
                else -> stack.getOrDefault(RG_GYM_TYPE_COMPONENT, defaultElementalTypes.random())
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
            val intermediate = when (this.coerceAtLeast(1) == 1) {
                true -> tl(modId("item.component.shiny_boost.guaranteed")).withStyle(ChatFormatting.UNDERLINE)
                else -> "1/${(Cobblemon.config.shinyRate.toInt() - this)}"
            }

            val tooltipText = tl(
                modId("item.component.shiny_boost"),
                intermediate,
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
}

class CommonPokeCache : PokeCache(Rarity.COMMON)

class UncommonPokeCache : PokeCache(Rarity.UNCOMMON)

class RarePokeCache : PokeCache(Rarity.RARE)

class EpicPokeCache : PokeCache(Rarity.EPIC)
