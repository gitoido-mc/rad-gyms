/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.CobblemonItems.ELIXIR
import com.cobblemon.mod.common.CobblemonItems.ETHER
import com.cobblemon.mod.common.CobblemonItems.HYPER_POTION
import com.cobblemon.mod.common.CobblemonItems.MAX_ELIXIR
import com.cobblemon.mod.common.CobblemonItems.MAX_ETHER
import com.cobblemon.mod.common.CobblemonItems.MAX_POTION
import com.cobblemon.mod.common.CobblemonItems.MAX_REVIVE
import com.cobblemon.mod.common.CobblemonItems.POTION
import com.cobblemon.mod.common.CobblemonItems.REVIVE
import com.cobblemon.mod.common.CobblemonItems.SUPER_POTION
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.ANY_HELD_ITEM
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.EVOLUTION_ITEMS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.RESTORES
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_1_POKE_BALLS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_1_POKE_BALL_MATERIALS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_2_POKE_BALLS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_2_POKE_BALL_MATERIALS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_3_POKE_BALLS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_3_POKE_BALL_MATERIALS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_4_POKE_BALLS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TIER_4_POKE_BALL_MATERIALS
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.TUMBLESTONES
import com.cobblemon.mod.common.api.tags.CobblemonItemTags.VITAMINS
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsLootTables.COMMON_LOOT_TABLE
import lol.gito.radgyms.common.registry.RadGymsLootTables.EPIC_LOOT_TABLE
import lol.gito.radgyms.common.registry.RadGymsLootTables.RARE_LOOT_TABLE
import lol.gito.radgyms.common.registry.RadGymsLootTables.SHARED_LOOT_TABLE
import lol.gito.radgyms.common.registry.RadGymsLootTables.UNCOMMON_LOOT_TABLE
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items.LAPIS_LAZULI
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.TagEntry
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class GymLootDataProvider(output: FabricDataOutput, lookup: CompletableFuture<HolderLookup.Provider>) :
    SimpleFabricLootTableProvider(output, lookup, LootContextParamSets.ALL_PARAMS) {
    companion object {
        const val COMMON_ROLLS = 5f
        const val COMMON_BONUS_ROLLS = .5f
        const val SHARED_ROLLS = 2f
        const val ITEM_BONUS_ROLLS = .25f
    }

    override fun generate(lootTableBiConsumer: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        lootTableBiConsumer.accept(
            SHARED_LOOT_TABLE,
            generateSharedDefaultLootTable(),
        )
        lootTableBiConsumer.accept(
            COMMON_LOOT_TABLE,
            generateCommonLootTable(),
        )
        lootTableBiConsumer.accept(
            UNCOMMON_LOOT_TABLE,
            generateUncommonLootTable(),
        )
        lootTableBiConsumer.accept(
            RARE_LOOT_TABLE,
            generateRareLootTable(),
        )
        lootTableBiConsumer.accept(
            EPIC_LOOT_TABLE,
            generateEpicLootTable(),
        )
    }

    private fun generateSharedDefaultLootTable() = LootTable
        .Builder()
        .pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(0f, SHARED_ROLLS))
                .setBonusRolls(ConstantValue.exactly(ITEM_BONUS_ROLLS))
                .add(TagEntry.expandTag(ANY_HELD_ITEM))
                .add(TagEntry.expandTag(EVOLUTION_ITEMS))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(0f, SHARED_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(TagEntry.expandTag(TUMBLESTONES))
                .add(TagEntry.expandTag(RESTORES))
                .add(TagEntry.expandTag(VITAMINS))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(LootItem.lootTableItem(LAPIS_LAZULI))
                .build(),
        )

    private fun generateCommonLootTable() = LootTable
        .Builder()
        .pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(0f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(TagEntry.expandTag(TIER_1_POKE_BALLS))
                .add(TagEntry.expandTag(TIER_1_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(POTION))
                .add(LootItem.lootTableItem(ETHER))
                .add(LootItem.lootTableItem(REVIVE))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(0f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(LootItem.lootTableItem(RadGymsItems.SHARD_COMMON))
                .build(),
        )

    private fun generateUncommonLootTable() = LootTable
        .Builder()
        .pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(TagEntry.expandTag(TIER_2_POKE_BALLS))
                .add(TagEntry.expandTag(TIER_2_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(SUPER_POTION))
                .add(LootItem.lootTableItem(MAX_ETHER))
                .add(LootItem.lootTableItem(REVIVE))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(LootItem.lootTableItem(SHARD_UNCOMMON))
                .build(),
        )

    private fun generateRareLootTable() = LootTable
        .Builder()
        .pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(TagEntry.expandTag(TIER_3_POKE_BALLS))
                .add(TagEntry.expandTag(TIER_3_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(HYPER_POTION))
                .add(LootItem.lootTableItem(ELIXIR))
                .add(LootItem.lootTableItem(REVIVE))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(LootItem.lootTableItem(SHARD_RARE))
                .build(),
        )

    private fun generateEpicLootTable() = LootTable
        .Builder()
        .pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(TagEntry.expandTag(TIER_4_POKE_BALLS))
                .add(TagEntry.expandTag(TIER_4_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(MAX_POTION))
                .add(LootItem.lootTableItem(MAX_ELIXIR))
                .add(LootItem.lootTableItem(MAX_REVIVE))
                .build(),
        ).pool(
            LootPool
                .lootPool()
                .setRolls(UniformGenerator.between(1f, COMMON_ROLLS))
                .setBonusRolls(ConstantValue.exactly(COMMON_BONUS_ROLLS))
                .add(LootItem.lootTableItem(SHARD_EPIC))
                .build(),
        )
}
