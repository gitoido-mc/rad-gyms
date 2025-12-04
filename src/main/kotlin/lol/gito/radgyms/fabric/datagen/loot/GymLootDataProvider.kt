/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.loot

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.tags.CobblemonItemTags
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsLootTables
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.TagEntry
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class GymLootDataProvider(
    output: FabricDataOutput,
    lookup: CompletableFuture<HolderLookup.Provider>,
) : SimpleFabricLootTableProvider(output, lookup, LootContextParamSets.ALL_PARAMS) {
    override fun generate(lootTableBiConsumer: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        lootTableBiConsumer.accept(
            RadGymsLootTables.SHARED_LOOT_TABLE,
            generateSharedDefaultLootTable()
        )
        lootTableBiConsumer.accept(
            RadGymsLootTables.COMMON_LOOT_TABLE,
            generateCommonLootTable()
        )
        lootTableBiConsumer.accept(
            RadGymsLootTables.UNCOMMON_LOOT_TABLE,
            generateUncommonLootTable()
        )
        lootTableBiConsumer.accept(
            RadGymsLootTables.RARE_LOOT_TABLE,
            generateRareLootTable()
        )
        lootTableBiConsumer.accept(
            RadGymsLootTables.EPIC_LOOT_TABLE,
            generateEpicLootTable()
        )
    }

    private fun generateSharedDefaultLootTable() = LootTable.Builder()
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(0f, 2f))
                .setBonusRolls(ConstantValue.exactly(.25f))
                .add(TagEntry.expandTag(CobblemonItemTags.ANY_HELD_ITEM))
                .add(TagEntry.expandTag(CobblemonItemTags.EVOLUTION_ITEMS))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(0f, 2f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(TagEntry.expandTag(CobblemonItemTags.TUMBLESTONES))
                .add(TagEntry.expandTag(CobblemonItemTags.RESTORES))
                .add(TagEntry.expandTag(CobblemonItemTags.VITAMINS))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI))
                .build()
        )

    private fun generateCommonLootTable() = LootTable.Builder()
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(0f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_1_POKE_BALLS))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_1_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(CobblemonItems.POTION))
                .add(LootItem.lootTableItem(CobblemonItems.ETHER))
                .add(LootItem.lootTableItem(CobblemonItems.REVIVE))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(0f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(LootItem.lootTableItem(RadGymsItems.SHARD_COMMON))
                .build()
        )

    private fun generateUncommonLootTable() = LootTable.Builder()
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_2_POKE_BALLS))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_2_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(CobblemonItems.SUPER_POTION))
                .add(LootItem.lootTableItem(CobblemonItems.MAX_ETHER))
                .add(LootItem.lootTableItem(CobblemonItems.REVIVE))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(LootItem.lootTableItem(RadGymsItems.SHARD_UNCOMMON))
                .build()
        )

    private fun generateRareLootTable() = LootTable.Builder()
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_3_POKE_BALLS))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_3_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(CobblemonItems.HYPER_POTION))
                .add(LootItem.lootTableItem(CobblemonItems.ELIXIR))
                .add(LootItem.lootTableItem(CobblemonItems.REVIVE))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(LootItem.lootTableItem(RadGymsItems.SHARD_RARE))
                .build()
        )

    private fun generateEpicLootTable() = LootTable.Builder()
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_4_POKE_BALLS))
                .add(TagEntry.expandTag(CobblemonItemTags.TIER_4_POKE_BALL_MATERIALS))
                .add(LootItem.lootTableItem(CobblemonItems.MAX_POTION))
                .add(LootItem.lootTableItem(CobblemonItems.MAX_ELIXIR))
                .add(LootItem.lootTableItem(CobblemonItems.MAX_REVIVE))
                .build()
        )
        .pool(
            LootPool.lootPool()
                .setRolls(UniformGenerator.between(1f, 5f))
                .setBonusRolls(ConstantValue.exactly(.5f))
                .add(LootItem.lootTableItem(RadGymsItems.SHARD_EPIC))
                .build()
        )
}
