/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.LOGGER
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.item.PokeShardBase
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams

class GenerateRewardHandler(event: GymEvents.GenerateRewardEvent) {
    init {
        val bundle = ItemStack(Items.BUNDLE)
        val bundleContents = BundleContents.Mutable(BundleContents.EMPTY)
        event.template
            .lootTables
            .filter {
                event.level in it.minLevel..it.maxLevel
            }
            .forEach { table ->
                debug(
                    "Settling level %d %s type rewards for player %s after beating leader".format(
                        event.level,
                        event.type,
                        event.player.name.tryCollapseToString()
                    )
                )
                val registryLootTable = event.player
                    .server
                    .reloadableRegistries()
                    .get()
                    .registryOrThrow(Registries.LOOT_TABLE)
                    .get(ResourceLocation.parse(table.id)) ?: return@forEach

                val lootContextParameterSet = LootParams.Builder(event.player.level() as ServerLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, event.player)
                    .withParameter(LootContextParams.ORIGIN, event.player.position())
                    .create(LootContextParamSets.GIFT)

                registryLootTable.getRandomItems(lootContextParameterSet).let { loot ->
                    if (RadGyms.CONFIG.shardRewards == true) {
                        event.rewards.addAll(loot)
                    } else {
                        event.rewards.addAll(loot.filter { it.item !is PokeShardBase })
                    }
                }
            }


        if (event.rewards.count() > BundleItem.DEFAULT_MAX_STACK_SIZE) {
            LOGGER.warn(
                "Reward bundle default stack size (%d) overflow (passed %d stacks of items), splitting...".format(
                    BundleItem.DEFAULT_MAX_STACK_SIZE,
                    event.rewards.count()
                ),
            )

            event.rewards.chunked(BundleItem.DEFAULT_MAX_STACK_SIZE).forEach {
                createBundle(event, bundle, bundleContents, it)
            }
        } else {
            createBundle(event, bundle, bundleContents, event.rewards)
        }

    }

    private fun createBundle(
        event: GymEvents.GenerateRewardEvent,
        bundle: ItemStack,
        bundleContents: BundleContents.Mutable,
        rewards: List<ItemStack>
    ) {
        rewards.forEach { bundleContents.tryInsert(it) }

        val styledLevel = Component.literal(event.level.toString()).withStyle(ChatFormatting.GOLD)
        val styledType = translatable(cobblemonResource("type.${event.type.lowercase()}").toLanguageKey())
            .setStyle(
                Style.EMPTY.withColor(ChatFormatting.GREEN).withItalic(true)
            )

        bundle.set(
            DataComponents.CUSTOM_NAME,
            translatable(
                modId("gym_reward").toLanguageKey("item"),
                styledLevel, styledType
            )
        )
        bundle.set(DataComponents.BUNDLE_CONTENTS, bundleContents.toImmutable())
        bundle.set(RadGymsDataComponents.RG_GYM_BUNDLE_COMPONENT, true)
        if (!event.player.addItem(bundle)) {
            ItemEntity(
                event.player.level(),
                event.player.x,
                event.player.y,
                event.player.z,
                bundle,
            ).also {
                event.player.level().addFreshEntity(it)
            }

        }
    }
}