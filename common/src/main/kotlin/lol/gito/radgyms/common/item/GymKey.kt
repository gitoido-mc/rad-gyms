/*
 * Copyright (c) 2025. gitoido-mc
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
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.net.server.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.util.TranslationUtil.buildPrefixedSuffixedTypeText
import lol.gito.radgyms.common.util.averagePokePartyLevel
import lol.gito.radgyms.common.util.displayClientMessage
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

class GymKey : CobblemonItem(
    Properties().rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand))

        RadGymsTemplates.templates.forEach { (key, value) ->
            debug("Found template")
            debug("Type: $key")
            debug("Interior: ${value.template}")
        }

        val party = (player as ServerPlayer).party()
        return when {
            (party.occupied() < MIN_PLAYER_TEAM_SIZE) -> {
                player.displayClientMessage(
                    translatable(modId("message.info.gym_entrance_party_empty").toLanguageKey())
                )
                debug("Player ${player.uuid} tried to use gym key with empty party, denying...")
                InteractionResultHolder.fail(player.getItemInHand(hand))
            }

            (party.all { it.isFainted() }) -> {
                player.displayClientMessage(
                    translatable(modId("message.info.gym_entrance_party_fainted").toLanguageKey())
                )
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
