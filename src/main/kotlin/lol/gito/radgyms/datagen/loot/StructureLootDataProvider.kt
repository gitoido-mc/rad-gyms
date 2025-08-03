/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.datagen.loot

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.registry.DataComponentRegistry
import lol.gito.radgyms.common.registry.ItemRegistry
import lol.gito.radgyms.common.registry.StructureLootTableRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetComponentsLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class StructureLootDataProvider(
    output: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : SimpleFabricLootTableProvider(output, registryLookup, LootContextTypes.ARCHAEOLOGY) {
    val commonItems = mapOf(
        Items.FLINT to 50,
        Items.RAW_IRON to 10,
        Items.LAPIS_LAZULI to 25,
        Items.RAW_COPPER to 25,
        Items.RAW_GOLD to 5,
        ItemRegistry.EXIT_ROPE to 10,
        ItemRegistry.GYM_KEY to 3
    )

    override fun accept(lootTableBiConsumer: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>) {
        val structureTypeMap = mapOf(
            StructureLootTableRegistry.BUG_GRAVEL to ElementalTypes.BUG,
            StructureLootTableRegistry.DARK_GRAVEL to ElementalTypes.DARK,
            StructureLootTableRegistry.DRAGON_GRAVEL to ElementalTypes.DRAGON,
            StructureLootTableRegistry.ELECTRIC_GRAVEL to ElementalTypes.ELECTRIC,
            StructureLootTableRegistry.FAIRY_GRAVEL to ElementalTypes.FAIRY,
            StructureLootTableRegistry.FIGHTING_GRAVEL to ElementalTypes.FIGHTING,
            StructureLootTableRegistry.FIRE_GRAVEL to ElementalTypes.FIRE,
            StructureLootTableRegistry.FLYING_GRAVEL to ElementalTypes.FLYING,
            StructureLootTableRegistry.GHOST_GRAVEL to ElementalTypes.GHOST,
            StructureLootTableRegistry.GRASS_GRAVEL to ElementalTypes.GRASS,
            StructureLootTableRegistry.GROUND_GRAVEL to ElementalTypes.GROUND,
            StructureLootTableRegistry.ICE_GRAVEL to ElementalTypes.ICE,
            StructureLootTableRegistry.NORMAL_GRAVEL to ElementalTypes.NORMAL,
            StructureLootTableRegistry.POISON_GRAVEL to ElementalTypes.POISON,
            StructureLootTableRegistry.PSYCHIC_GRAVEL to ElementalTypes.PSYCHIC,
            StructureLootTableRegistry.ROCK_GRAVEL to ElementalTypes.ROCK,
            StructureLootTableRegistry.STEEL_GRAVEL to ElementalTypes.STEEL,
            StructureLootTableRegistry.WATER_GRAVEL to ElementalTypes.WATER,
        )

        structureTypeMap.forEach { (id, type) ->
            val pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(1f))

            typedItemList(type).plus(commonItems).forEach { (item, weight) ->
                if (item != ItemRegistry.GYM_KEY) {
                    pool.also { it.with(ItemEntry.builder(item).weight(weight)) }
                } else {
                    val componentsFunction = SetComponentsLootFunction.builder(
                        DataComponentRegistry.GYM_TYPE_COMPONENT,
                        type.name
                    )

                    pool.also { it.with(ItemEntry.builder(item).apply(componentsFunction).weight(weight)) }
                }
            }

            lootTableBiConsumer.accept(id, LootTable.builder().pool(pool))
        }

        val commonLootPool = LootPool.builder().rolls(ConstantLootNumberProvider.create(1f))

        commonItems.forEach { (item, weight) ->
            commonLootPool.also { it.with(ItemEntry.builder(item).weight(weight)) }
        }

        lootTableBiConsumer.accept(
            StructureLootTableRegistry.COMMON_GRAVEL,
            LootTable.builder().pool(commonLootPool)
        )
    }

    private fun typedItemList(type: ElementalType): Map<Item, Int> {
        return when (type) {
            ElementalTypes.BUG -> mapOf(
                CobblemonItems.BUG_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.DARK -> mapOf(
                CobblemonItems.DARK_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.DRAGON -> mapOf(
                CobblemonItems.DRAGON_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.ELECTRIC -> mapOf(
                CobblemonItems.ELECTRIC_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.FAIRY -> mapOf(
                CobblemonItems.FAIRY_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.FIGHTING -> mapOf(
                CobblemonItems.FIGHTING_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.FIRE -> mapOf(
                CobblemonItems.FIRE_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.FLYING -> mapOf(
                CobblemonItems.FLYING_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.GHOST -> mapOf(
                CobblemonItems.GHOST_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.GRASS -> mapOf(
                CobblemonItems.GRASS_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.GROUND -> mapOf(
                CobblemonItems.GROUND_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.ICE -> mapOf(
                CobblemonItems.ICE_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.NORMAL -> mapOf(
                CobblemonItems.NORMAL_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.POISON -> mapOf(
                CobblemonItems.POISON_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.PSYCHIC -> mapOf(
                CobblemonItems.PSYCHIC_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.ROCK -> mapOf(
                CobblemonItems.ROCK_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.STEEL -> mapOf(
                CobblemonItems.STEEL_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            ElementalTypes.WATER -> mapOf(
                CobblemonItems.WATER_GEM to 5,
                ItemRegistry.GYM_KEY to 3
            )

            else -> emptyMap()
        }
    }
}
