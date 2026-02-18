/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.common.MIN_PLAYER_TEAM_SIZE
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.extension.averagePokePartyLevel
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.ElementalTypeTranslationHelper.buildPrefixedSuffixedTypeText
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.net.server.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsDimensions
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class GymKey : CobblemonItem(
    Properties().rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand))

        val party = (player as ServerPlayer).party()
        return when {
            (level.dimension() == RadGymsDimensions.GYM_DIMENSION) -> {
                player.displayClientMessage(tl("message.info.gym_key_wrong_place"))
                InteractionResultHolder.fail(player.getItemInHand(hand))
            }

            (party.occupied() < MIN_PLAYER_TEAM_SIZE) -> {
                player.displayClientMessage(tl("message.info.gym_entrance_party_empty"))
                debug("Player ${player.uuid} tried to use gym key with empty party, denying...")
                InteractionResultHolder.fail(player.getItemInHand(hand))
            }

            (party.all { it.isFainted() }) -> {
                player.displayClientMessage(tl("message.info.gym_entrance_party_fainted"))
                debug("Player ${player.uuid} tried to use gym key with party fainted, denying...")
                InteractionResultHolder.fail(player.getItemInHand(hand))
            }

            else -> {
                val derivedLevel = when (RadGyms.CONFIG.deriveAverageGymLevel!!) {
                    true -> player.averagePokePartyLevel()
                    false -> RadGyms.CONFIG.minLevel!!
                }
                val type = player.getItemInHand(hand).getOrDefault(
                    RadGymsDataComponents.RG_GYM_TYPE_COMPONENT,
                    "chaos"
                )

                OpenGymEnterScreenS2C(derivedLevel, true, type).sendToPlayer(player)

                InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), true)
            }
        }
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        type: TooltipFlag
    ) {
        val attuned = itemStack.get(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT)
        tooltip.add(buildPrefixedSuffixedTypeText(attuned))
    }

    override fun getDefaultInstance(): ItemStack = ItemStack(this).apply {
        set(DataComponents.RARITY, Rarity.UNCOMMON)
    }
}
