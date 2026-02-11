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
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.cache.CacheHandler
import lol.gito.radgyms.common.registry.RadGymsDataComponents.RG_CACHE_SHINY_BOOST_COMPONENT
import lol.gito.radgyms.common.registry.RadGymsDataComponents.RG_GYM_TYPE_COMPONENT
import lol.gito.radgyms.common.util.TranslationUtil.buildPrefixedSuffixedTypeText
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents.RARITY
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

const val RG_CACHE_BLOCK_BOOST = 9

open class PokeCache(private val rarity: Rarity) : CobblemonItem(Properties().rarity(rarity)) {
    override fun use(level: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), true)
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail(user.getItemInHand(hand))

        val stack = user.getItemInHand(hand)
        val offhand = user.offhandItem
        var boost = stack.getOrDefault(RG_CACHE_SHINY_BOOST_COMPONENT, 0)
        val type = when (stack.getOrDefault(RG_GYM_TYPE_COMPONENT, ElementalTypes.all().random().showdownId)) {
            "chaos" -> ElementalTypes.all().random().showdownId
            else -> stack.getOrDefault(RG_GYM_TYPE_COMPONENT, ElementalTypes.all().random().showdownId)
        }
        if (offhand.`is`(Items.LAPIS_LAZULI) && boost < Cobblemon.config.shinyRate) {
            boost = boost.plus(CONFIG.lapisBoostAmount!!)
            stack.set(RG_CACHE_SHINY_BOOST_COMPONENT, boost)
        }
        if (offhand.`is`(Items.LAPIS_BLOCK) && boost < Cobblemon.config.shinyRate) {
            boost = when (boost.plus(RG_CACHE_BLOCK_BOOST) > Cobblemon.config.shinyRate) {
                true -> Cobblemon.config.shinyRate.toInt()
                false -> boost.plus(RG_CACHE_BLOCK_BOOST * CONFIG.lapisBoostAmount!!)
                    .coerceAtMost(Cobblemon.config.shinyRate.toInt() - 1)
            }
            stack.set(RG_CACHE_SHINY_BOOST_COMPONENT, boost)
        }

        if (offhand.`is`(Items.LAPIS_LAZULI) || offhand.`is`(Items.LAPIS_BLOCK)) {
            offhand.consume(1, user)
            return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), true)
        }

        val rarity = stack.getOrDefault(RARITY, Rarity.COMMON)
        val poke: Pokemon = CacheHandler.getPoke(
            type,
            rarity,
            user as ServerPlayer,
            boost
        )

        CACHE_ROLL_POKE.emit(
            GymEvents.CacheRollPokeEvent(
                user,
                poke,
                type,
                rarity,
                boost
            )
        )

        return InteractionResultHolder.success(user.getItemInHand(hand))
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        type: TooltipFlag
    ) {
        val shinyBoost = stack.get(RG_CACHE_SHINY_BOOST_COMPONENT)
        val cacheType = stack.get(RG_GYM_TYPE_COMPONENT)
        if (shinyBoost != null && shinyBoost > 0) {
            val tooltipText = translatable(
                modId("item.component.shiny_boost").toLanguageKey(),
                "1/${(Cobblemon.config.shinyRate.toInt() - shinyBoost).coerceAtLeast(1)}"
            )

            tooltip.add(tooltipText.withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD))
        }

        val tooltipText: Component = buildPrefixedSuffixedTypeText(cacheType)
        tooltip.add(tooltipText)
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
