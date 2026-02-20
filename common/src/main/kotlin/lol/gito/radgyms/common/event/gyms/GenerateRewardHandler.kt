/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.common.MAX_PERFECT_IVS
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.LOGGER
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.reward.AdvancementReward
import lol.gito.radgyms.common.api.dto.reward.CommandReward
import lol.gito.radgyms.common.api.dto.reward.LootTableReward
import lol.gito.radgyms.common.api.dto.reward.PokemonReward
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.extension.rainbow
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.helper.tlc
import lol.gito.radgyms.common.item.PokeShardBase
import lol.gito.radgyms.common.registry.RadGymsItems
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams

class GenerateRewardHandler(
    val event: GymEvents.GenerateRewardEvent,
) {
    init {
        debug(
            "Settling level %d %s type rewards for player %s after beating leader".format(
                event.level,
                event.type,
                event.player.name.tryCollapseToString(),
            ),
        )

        val currentLevelRewards =
            event.template
                .rewards
                .filter {
                    event.level in it.minLevel..it.maxLevel
                }

        event.player.server.sendSystemMessage(Component.literal("say test"))

        handleLootRewards(currentLevelRewards.filterIsInstance<LootTableReward>())
        handlePokemonRewards(currentLevelRewards.filterIsInstance<PokemonReward>())
        handleCommandRewards(currentLevelRewards.filterIsInstance<CommandReward>())
        handleAdvancementRewards(currentLevelRewards.filterIsInstance<AdvancementReward>())
    }

    private fun handleAdvancementRewards(rewards: List<AdvancementReward>) {
        val advancements = event.player.server.advancements.allAdvancements

        rewards.forEach { reward ->
            val advancement =
                advancements.firstNotNullOfOrNull {
                    if (it.id == ResourceLocation.parse(reward.id)) return@firstNotNullOfOrNull it
                    return@firstNotNullOfOrNull null
                }

            advancement?.let { holder ->
                val progress = event.player.advancements.getOrStartProgress(holder)
                if (progress.isDone) return@let

                progress.remainingCriteria.forEach { criteria ->
                    event.player.advancements.award(holder, criteria)
                }
            }
        }
    }

    private fun handleCommandRewards(rewards: List<CommandReward>) {
        val handler = event.player.server.commands

        rewards.forEach {
            val source =
                when (it.asServer) {
                    true ->
                        event.player.server
                            .createCommandSourceStack()
                            .withPermission(it.opLevel)
                    false -> event.player.createCommandSourceStack().withPermission(it.opLevel)
                }

            handler.performPrefixedCommand(source, it.execute.trim())
        }
    }

    private fun handlePokemonRewards(rewards: List<PokemonReward>) {
        rewards.forEach {
            if (it.minPerfectIvs != null) {
                it.pokemon.minPerfectIVs = it.minPerfectIvs.coerceIn(0, MAX_PERFECT_IVS)
            }
            val poke = it.pokemon.create(event.player)
            event.player.party().add(poke)

            event.player.displayClientMessage(
                tl(
                    "message.info.gym_complete.reward_poke",
                    when (poke.shiny) {
                        true -> poke.species.translatedName.rainbow()
                        false -> poke.species.translatedName
                    },
                ),
            )
        }
    }

    private fun handleLootRewards(rewards: List<LootTableReward>) {
        val bundle = ItemStack(RadGymsItems.GYM_REWARD)
        val bundleContents = BundleContents.Mutable(BundleContents.EMPTY)

        rewards.forEach { table ->
            val registryLootTable =
                event.player
                    .server
                    .reloadableRegistries()
                    .get()
                    .registryOrThrow(Registries.LOOT_TABLE)
                    .get(ResourceLocation.parse(table.id)) ?: return@forEach

            val lootContextParameterSet =
                LootParams
                    .Builder(event.player.level() as ServerLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, event.player)
                    .withParameter(LootContextParams.ORIGIN, event.player.position())
                    .create(LootContextParamSets.GIFT)

            registryLootTable.getRandomItems(lootContextParameterSet).let { loot ->
                if (table.maxItems != null && loot.count() > table.maxItems) {
                    addRewards(loot.shuffled().take(table.maxItems))
                    return@let
                }
                addRewards(loot)
            }
        }

        when (event.rewards.count() > BundleItem.DEFAULT_MAX_STACK_SIZE) {
            false -> createBundle(bundle, bundleContents, event.rewards)
            true -> {
                LOGGER.warn(
                    "Reward bundle default stack size (%d) overflow (passed %d stacks of items), splitting...".format(
                        BundleItem.DEFAULT_MAX_STACK_SIZE,
                        event.rewards.count(),
                    ),
                )

                event.rewards.chunked(BundleItem.DEFAULT_MAX_STACK_SIZE).forEach {
                    createBundle(bundle, bundleContents, it)
                }
            }
        }
    }

    private fun createBundle(
        bundle: ItemStack,
        bundleContents: BundleContents.Mutable,
        rewards: List<ItemStack>,
    ) {
        rewards.forEach { bundleContents.tryInsert(it) }

        val styledLevel = Component.literal(event.level.toString()).withStyle(ChatFormatting.GOLD)
        val styledType =
            tlc("type.${event.type.lowercase()}").setStyle(
                Style.EMPTY.withColor(ChatFormatting.GREEN).withItalic(true),
            )

        bundle.set(
            DataComponents.CUSTOM_NAME,
            tl(prefix = "item", key = "gym_reward", styledLevel, styledType),
        )
        bundle.set(DataComponents.BUNDLE_CONTENTS, bundleContents.toImmutable())
        event.player.giveOrDropItemStack(bundle, true)
    }

    private fun addRewards(loot: List<ItemStack>) =
        when (RadGyms.config.shardRewards) {
            true -> event.rewards.addAll(loot)
            else -> event.rewards.addAll(loot.filter { it.item !is PokeShardBase })
        }
}
