/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsLootTables
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class StructureLootDataProvider(
    output: FabricDataOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : SimpleFabricLootTableProvider(output, registryLookup, LootContextParamSets.ARCHAEOLOGY) {
    companion object {
        const val FLINT_WEIGHT = 50
        const val IRON_INGOT_WEIGHT = 10
        const val LAPIS_LAZULI_WEIGHT = 25
        const val RAW_COPPER_WEIGHT = 25
        const val RAW_GOLD_WEIGHT = 5
        const val EXIT_ROPE_WEIGHT = 10
        const val GYM_KEY_WEIGHT = 3
        const val TYPED_GEM_WEIGHT = 5
    }

    @Suppress("MagicNumber")
    val commonItemWeight = mapOf(
        Items.FLINT to FLINT_WEIGHT,
        Items.RAW_IRON to IRON_INGOT_WEIGHT,
        Items.LAPIS_LAZULI to LAPIS_LAZULI_WEIGHT,
        Items.RAW_COPPER to RAW_COPPER_WEIGHT,
        Items.RAW_GOLD to RAW_GOLD_WEIGHT,
        RadGymsItems.EXIT_ROPE to EXIT_ROPE_WEIGHT,
        RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
    )

    override fun generate(lootTableBiConsumer: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        val structureTypeMap = mapOf(
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

        structureTypeMap.forEach { (id, type) ->
            val pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1f))

            typedItemList(type).plus(commonItemWeight).forEach { (item, weight) ->
                if (item != RadGymsItems.GYM_KEY) {
                    pool.also { it.add(LootItem.lootTableItem(item).setWeight(weight)) }
                } else {
                    val componentsFunction = SetComponentsFunction.setComponent(
                        RadGymsDataComponents.RG_GYM_TYPE_COMPONENT,
                        type.showdownId
                    )

                    pool.also { it.add(LootItem.lootTableItem(item).apply(componentsFunction).setWeight(weight)) }
                }
            }

            lootTableBiConsumer.accept(id, LootTable.lootTable().pool(pool.build()))
        }

        val commonLootPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1f))

        commonItemWeight.forEach { (item, weight) ->
            commonLootPool.also { it.add(LootItem.lootTableItem(item).setWeight(weight)) }
        }

        lootTableBiConsumer.accept(
            RadGymsLootTables.COMMON_GRAVEL,
            LootTable.lootTable().pool(commonLootPool.build())
        )
    }

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    private fun typedItemList(type: ElementalType): Map<Item, Int> = when (type) {
        ElementalTypes.BUG -> mapOf(
            CobblemonItems.BUG_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.DARK -> mapOf(
            CobblemonItems.DARK_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.DRAGON -> mapOf(
            CobblemonItems.DRAGON_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.ELECTRIC -> mapOf(
            CobblemonItems.ELECTRIC_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.FAIRY -> mapOf(
            CobblemonItems.FAIRY_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.FIGHTING -> mapOf(
            CobblemonItems.FIGHTING_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.FIRE -> mapOf(
            CobblemonItems.FIRE_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.FLYING -> mapOf(
            CobblemonItems.FLYING_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.GHOST -> mapOf(
            CobblemonItems.GHOST_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.GRASS -> mapOf(
            CobblemonItems.GRASS_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.GROUND -> mapOf(
            CobblemonItems.GROUND_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.ICE -> mapOf(
            CobblemonItems.ICE_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.NORMAL -> mapOf(
            CobblemonItems.NORMAL_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.POISON -> mapOf(
            CobblemonItems.POISON_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.PSYCHIC -> mapOf(
            CobblemonItems.PSYCHIC_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.ROCK -> mapOf(
            CobblemonItems.ROCK_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.STEEL -> mapOf(
            CobblemonItems.STEEL_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        ElementalTypes.WATER -> mapOf(
            CobblemonItems.WATER_GEM to TYPED_GEM_WEIGHT,
            RadGymsItems.GYM_KEY to GYM_KEY_WEIGHT
        )

        else -> emptyMap()
    }
}
