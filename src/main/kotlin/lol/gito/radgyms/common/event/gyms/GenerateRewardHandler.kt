/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.api.event.ModEvents
import lol.gito.radgyms.common.item.PokeShardBase
import lol.gito.radgyms.common.registry.DataComponentRegistry
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.BundleContentsComponent
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BundleItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting

class GenerateRewardHandler(event: ModEvents.GenerateRewardEvent) {
    init {
        val bundle = ItemStack(Items.BUNDLE)
        val bundleContents = BundleContentsComponent.Builder(BundleContentsComponent.DEFAULT)
        event.template
            .lootTables
            .filter {
                event.level in it.levels.first..it.levels.second
            }
            .forEach { table ->
                debug(
                    "Settling level %d %s type rewards for player %s after beating leader".format(
                        event.level,
                        event.type,
                        event.player.name.literalString
                    )
                )
                val registryLootTable = event.player
                    .server
                    .reloadableRegistries
                    .registryManager
                    .get(RegistryKeys.LOOT_TABLE)
                    .get(table.id) ?: return@forEach

                val lootContextParameterSet = LootContextParameterSet.Builder(event.player.world as ServerWorld)
                    .add(LootContextParameters.THIS_ENTITY, event.player)
                    .add(LootContextParameters.ORIGIN, event.player.pos)
                    .build(LootContextTypes.GIFT)

                registryLootTable.generateLoot(lootContextParameterSet).let { loot ->
                    if (RadGyms.CONFIG.shardRewards == true) {
                        event.rewards.addAll(loot)
                    } else {
                        event.rewards.addAll(loot.filter { it.item !is PokeShardBase })
                    }
                }
            }


        if (event.rewards.count() > BundleItem.DEFAULT_MAX_COUNT) {
            LOGGER.warn(
                "Reward bundle default stack size (%d) overflow (passed %d stacks of items), splitting...".format(
                    BundleItem.DEFAULT_MAX_COUNT,
                    event.rewards.count()
                ),
            )

            event.rewards.chunked(BundleItem.DEFAULT_MAX_COUNT).forEach {
                createBundle(event, bundle, bundleContents, it)
            }
        } else {
            createBundle(event, bundle, bundleContents, event.rewards)
        }

    }

    private fun createBundle(
        event: ModEvents.GenerateRewardEvent,
        bundle: ItemStack,
        bundleContents: BundleContentsComponent.Builder,
        rewards: List<ItemStack>
    ) {
        rewards.forEach { bundleContents.add(it) }

        val styledLevel = MutableText.of(Text.literal(event.level.toString()).content).formatted(Formatting.GOLD)
        val styledType = translatable(cobblemonResource("type.${event.type.lowercase()}").toTranslationKey())
            .setStyle(
                Style.EMPTY.withColor(Formatting.GREEN).withItalic(true)
            )

        bundle.set(
            DataComponentTypes.CUSTOM_NAME,
            translatable(
                modId("gym_reward").toTranslationKey("item"),
                styledLevel, styledType
            )
        )
        bundle.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContents.build())
        bundle.set(DataComponentRegistry.RAD_GYM_BUNDLE_COMPONENT, true)
        if (!event.player.giveItemStack(bundle)) {
            ItemEntity(
                event.player.world,
                event.player.pos.x,
                event.player.pos.y,
                event.player.pos.z,
                bundle,
            ).let {
                event.player.world.spawnEntity(it)
            }

        }
    }
}