/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsLootTables
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class StructureLootDataProvider(output: FabricDataOutput, registryLookup: CompletableFuture<HolderLookup.Provider>) :
    SimpleFabricLootTableProvider(output, registryLookup, LootContextParamSets.ARCHAEOLOGY) {
    companion object {
        const val FLINT_WEIGHT = 50
        const val RAW_COPPER_WEIGHT = 25
        const val LAPIS_LAZULI_WEIGHT = 25
        const val RAW_IRON_WEIGHT = 10
        const val EXIT_ROPE_WEIGHT = 10
        const val RAW_GOLD_WEIGHT = 5
        const val GYM_KEY_WEIGHT = 3
        const val TYPED_GEM_WEIGHT = 5
    }

    val commonItems =
        listOf(
            Items.FLINT,
            Items.RAW_IRON,
            Items.LAPIS_LAZULI,
            Items.RAW_COPPER,
            Items.RAW_GOLD,
            RadGymsItems.EXIT_ROPE,
            RadGymsItems.GYM_KEY,
        )

    val gems =
        mapOf(
            ElementalTypes.BUG to CobblemonItems.BUG_GEM,
            ElementalTypes.DARK to CobblemonItems.DARK_GEM,
            ElementalTypes.DRAGON to CobblemonItems.DRAGON_GEM,
            ElementalTypes.ELECTRIC to CobblemonItems.ELECTRIC_GEM,
            ElementalTypes.FAIRY to CobblemonItems.FAIRY_GEM,
            ElementalTypes.FIGHTING to CobblemonItems.FIGHTING_GEM,
            ElementalTypes.FIRE to CobblemonItems.FIRE_GEM,
            ElementalTypes.FLYING to CobblemonItems.FLYING_GEM,
            ElementalTypes.GHOST to CobblemonItems.GHOST_GEM,
            ElementalTypes.GRASS to CobblemonItems.GRASS_GEM,
            ElementalTypes.GROUND to CobblemonItems.GROUND_GEM,
            ElementalTypes.ICE to CobblemonItems.ICE_GEM,
            ElementalTypes.NORMAL to CobblemonItems.NORMAL_GEM,
            ElementalTypes.POISON to CobblemonItems.POISON_GEM,
            ElementalTypes.PSYCHIC to CobblemonItems.PSYCHIC_GEM,
            ElementalTypes.ROCK to CobblemonItems.ROCK_GEM,
            ElementalTypes.STEEL to CobblemonItems.STEEL_GEM,
            ElementalTypes.WATER to CobblemonItems.WATER_GEM,
        )

    val structureTypeMap =
        mapOf(
            RadGymsLootTables.BUG_GRAVEL to ElementalTypes.BUG,
            RadGymsLootTables.DARK_GRAVEL to ElementalTypes.DARK,
            RadGymsLootTables.DRAGON_GRAVEL to ElementalTypes.DRAGON,
            RadGymsLootTables.ELECTRIC_GRAVEL to ElementalTypes.ELECTRIC,
            RadGymsLootTables.FAIRY_GRAVEL to ElementalTypes.FAIRY,
            RadGymsLootTables.FIGHTING_GRAVEL to ElementalTypes.FIGHTING,
            RadGymsLootTables.FIRE_GRAVEL to ElementalTypes.FIRE,
            RadGymsLootTables.FLYING_GRAVEL to ElementalTypes.FLYING,
            RadGymsLootTables.GHOST_GRAVEL to ElementalTypes.GHOST,
            RadGymsLootTables.GRASS_GRAVEL to ElementalTypes.GRASS,
            RadGymsLootTables.GROUND_GRAVEL to ElementalTypes.GROUND,
            RadGymsLootTables.ICE_GRAVEL to ElementalTypes.ICE,
            RadGymsLootTables.NORMAL_GRAVEL to ElementalTypes.NORMAL,
            RadGymsLootTables.POISON_GRAVEL to ElementalTypes.POISON,
            RadGymsLootTables.PSYCHIC_GRAVEL to ElementalTypes.PSYCHIC,
            RadGymsLootTables.ROCK_GRAVEL to ElementalTypes.ROCK,
            RadGymsLootTables.STEEL_GRAVEL to ElementalTypes.STEEL,
            RadGymsLootTables.WATER_GRAVEL to ElementalTypes.WATER,
        )

    override fun generate(lootTableBiConsumer: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        structureTypeMap.forEach { (id, type) ->
            val pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1f))

            commonItems.forEach { item ->
                val lootItem = LootItem.lootTableItem(item)
                val weight = getWeight(item)

                when (item) {
                    RadGymsItems.GYM_KEY -> {
                        val componentsFunction =
                            SetComponentsFunction.setComponent(
                                RadGymsDataComponents.RG_GYM_TYPE_COMPONENT,
                                type.showdownId,
                            )

                        pool.also { it.add(lootItem.apply(componentsFunction).setWeight(weight)) }
                    }

                    else -> pool.also { it.add(lootItem.setWeight(weight)) }
                }
            }

            val typeGem = gems[type] as ItemLike
            pool.add(LootItem.lootTableItem(typeGem).setWeight(getWeight(typeGem)))

            lootTableBiConsumer.accept(id, LootTable.lootTable().pool(pool.build()))
        }

        val commonLootPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1f))

        commonItems.forEach { item ->
            val weight = getWeight(item)
            commonLootPool.also { it.add(LootItem.lootTableItem(item).setWeight(weight)) }
        }

        lootTableBiConsumer.accept(
            RadGymsLootTables.COMMON_GRAVEL,
            LootTable.lootTable().pool(commonLootPool.build()),
        )
    }

    private fun getWeight(item: ItemLike): Int {
        val weight =
            when (item) {
                RadGymsItems.GYM_KEY -> GYM_KEY_WEIGHT
                RadGymsItems.EXIT_ROPE -> EXIT_ROPE_WEIGHT
                Items.FLINT -> FLINT_WEIGHT
                Items.RAW_IRON -> RAW_IRON_WEIGHT
                Items.RAW_COPPER -> RAW_COPPER_WEIGHT
                Items.RAW_GOLD -> RAW_GOLD_WEIGHT
                Items.LAPIS_LAZULI -> LAPIS_LAZULI_WEIGHT
                is CobblemonItem -> TYPED_GEM_WEIGHT
                else -> 0
            }
        return weight
    }
}
