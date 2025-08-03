/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.client.renderer.GymKeyRenderer
import lol.gito.radgyms.client.renderer.ISpecialItemModel
import lol.gito.radgyms.client.renderer.SpecialItemRenderer
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.registry.DataComponentRegistry
import lol.gito.radgyms.common.util.TranslationUtil.attuneType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.*
import net.minecraft.world.World
import java.util.function.Consumer
import java.util.stream.Stream

class GymKey : Item(
    Settings().also { settings ->
        settings
            .rarity(Rarity.UNCOMMON)
            .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
    }
), ISpecialItemModel {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
//        if (world.isClient) {
//            GuiHandler.openGymKeyScreen(user)
//        }

        return super.use(world, user, hand)
    }

    override fun appendTooltip(
        itemStack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val attuned = itemStack.get(DataComponentRegistry.GYM_TYPE_COMPONENT)
        if (attuned != null) {
            val tooltipText: MutableText = attuneType(attuned)
            tooltip.addLast(tooltipText.formatted(Formatting.GOLD))
        } else {
            val tooltipText: MutableText = translatable(
                modId("item.component.gym_type").toTranslationKey(),
                translatable(
                    cobblemonResource("type.suffix").toTranslationKey(),
                    translatable(modId("item.component.type.chaos").toTranslationKey()).styled {
                        it.withFormatting(Formatting.OBFUSCATED)
                    }
                )
            )
            tooltip.addLast(
                tooltipText.styled {
                    it.withColor(Formatting.DARK_PURPLE)
                }
            )
        }
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
                loader.accept(RadGyms.modModelId(it, "inventory"))
            }
        }
    }

    @Environment(EnvType.CLIENT)
    override fun getRenderer(): SpecialItemRenderer {
        return GymKeyRenderer.INSTANCE
    }
}
