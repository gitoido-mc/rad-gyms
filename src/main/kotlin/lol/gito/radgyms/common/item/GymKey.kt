/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.client.RadGymsClient
import lol.gito.radgyms.client.renderer.GymKeyRenderer
import lol.gito.radgyms.client.renderer.ISpecialItemModel
import lol.gito.radgyms.client.renderer.SpecialItemRenderer
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.network.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.registry.DataComponentRegistry
import lol.gito.radgyms.common.util.TranslationUtil.buildPrefixedSuffixedTypeText
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.function.Consumer
import java.util.stream.Stream

class GymKey : ISpecialItemModel, Item(
    Settings().also { settings ->
        settings
            .rarity(Rarity.UNCOMMON)
            .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
    }
) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            return TypedActionResult.pass(player.getStackInHand(hand))
        }


        val party = Cobblemon.implementation.server()!!.playerManager.getPlayer(player.uuid)!!.party()
        if (party.occupied() == 0) {
            player.sendMessage(translatable(modId("message.info.gym_entrance_party_empty").toTranslationKey()))
            debug("Player ${player.uuid} tried to use gym key with empty party, denying...")
            return TypedActionResult.fail(player.getStackInHand(hand))
        }

        if (party.all { it.isFainted() }) {
            player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))
            debug("Player ${player.uuid} tried to use gym key with party fainted, denying...")
            return TypedActionResult.fail(player.getStackInHand(hand))
        }

        ServerPlayNetworking.send(
            player as ServerPlayerEntity, OpenGymEnterScreenS2C(
                true,
                player.getStackInHand(hand).get(DataComponentRegistry.GYM_TYPE_COMPONENT)!!
            )
        )

        return TypedActionResult.success(player.getStackInHand(hand), true)
    }

    override fun appendTooltip(
        itemStack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val attuned = itemStack.get(DataComponentRegistry.GYM_TYPE_COMPONENT)
        tooltip.addLast(buildPrefixedSuffixedTypeText(attuned))
    }

    override fun getDefaultStack(): ItemStack {
        val itemStack = ItemStack(this)
        itemStack.set(DataComponentTypes.RARITY, Rarity.UNCOMMON)

        return itemStack
    }

    @Environment(EnvType.CLIENT)
    override fun loadModels(
        unbakedModels: Stream<Identifier>,
        loader: Consumer<ModelIdentifier>
    ) {
        unbakedModels.forEach {
            if (it.namespace.equals(RadGyms.MOD_ID) && it.path.startsWith("gym_key")) {
                loader.accept(RadGymsClient.modModelId(it, "inventory"))
            }
        }
    }

    @Environment(EnvType.CLIENT)
    override fun getRenderer(): SpecialItemRenderer {
        return GymKeyRenderer.INSTANCE
    }
}
