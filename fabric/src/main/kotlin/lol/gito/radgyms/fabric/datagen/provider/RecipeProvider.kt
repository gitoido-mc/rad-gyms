/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.CobblemonItems.BINDING_BAND
import com.cobblemon.mod.common.CobblemonItems.BUG_GEM
import com.cobblemon.mod.common.CobblemonItems.DARK_GEM
import com.cobblemon.mod.common.CobblemonItems.DRAGON_GEM
import com.cobblemon.mod.common.CobblemonItems.ELECTRIC_GEM
import com.cobblemon.mod.common.CobblemonItems.FAIRY_GEM
import com.cobblemon.mod.common.CobblemonItems.FIGHTING_GEM
import com.cobblemon.mod.common.CobblemonItems.FIRE_GEM
import com.cobblemon.mod.common.CobblemonItems.FLYING_GEM
import com.cobblemon.mod.common.CobblemonItems.GHOST_GEM
import com.cobblemon.mod.common.CobblemonItems.GRASS_GEM
import com.cobblemon.mod.common.CobblemonItems.GROUND_GEM
import com.cobblemon.mod.common.CobblemonItems.ICE_GEM
import com.cobblemon.mod.common.CobblemonItems.NORMAL_GEM
import com.cobblemon.mod.common.CobblemonItems.POISON_GEM
import com.cobblemon.mod.common.CobblemonItems.PSYCHIC_GEM
import com.cobblemon.mod.common.CobblemonItems.ROCK_GEM
import com.cobblemon.mod.common.CobblemonItems.STEEL_GEM
import com.cobblemon.mod.common.CobblemonItems.WATER_GEM
import com.cobblemon.mod.common.api.pokeball.PokeBalls.LUXURY_BALL
import com.cobblemon.mod.common.api.pokeball.PokeBalls.POKE_BALL
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.api.types.ElementalTypes.BUG
import com.cobblemon.mod.common.api.types.ElementalTypes.DARK
import com.cobblemon.mod.common.api.types.ElementalTypes.DRAGON
import com.cobblemon.mod.common.api.types.ElementalTypes.ELECTRIC
import com.cobblemon.mod.common.api.types.ElementalTypes.FAIRY
import com.cobblemon.mod.common.api.types.ElementalTypes.FIGHTING
import com.cobblemon.mod.common.api.types.ElementalTypes.FIRE
import com.cobblemon.mod.common.api.types.ElementalTypes.FLYING
import com.cobblemon.mod.common.api.types.ElementalTypes.GHOST
import com.cobblemon.mod.common.api.types.ElementalTypes.GRASS
import com.cobblemon.mod.common.api.types.ElementalTypes.GROUND
import com.cobblemon.mod.common.api.types.ElementalTypes.ICE
import com.cobblemon.mod.common.api.types.ElementalTypes.NORMAL
import com.cobblemon.mod.common.api.types.ElementalTypes.POISON
import com.cobblemon.mod.common.api.types.ElementalTypes.PSYCHIC
import com.cobblemon.mod.common.api.types.ElementalTypes.ROCK
import com.cobblemon.mod.common.api.types.ElementalTypes.STEEL
import com.cobblemon.mod.common.api.types.ElementalTypes.WATER
import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.item.GymKey
import lol.gito.radgyms.common.item.PokeCache
import lol.gito.radgyms.common.item.PokeShardBase
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_COMMON
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsItems.GYM_KEY
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_BLOCK_COMMON
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_BLOCK_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_BLOCK_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_BLOCK_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_COMMON
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_UNCOMMON
import lol.gito.radgyms.fabric.datagen.json.builder.ShapelessWithComponentRecipeJsonBuilder
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents.RARITY
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.*
import net.minecraft.world.item.Rarity
import oshi.util.tuples.Quartet
import java.util.concurrent.CompletableFuture

class RecipeProvider(
    output: FabricDataOutput,
    lookup: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, lookup) {
    companion object {
        const val BLOCK_TO_SHARD_AMOUNT = 9
    }

    override fun buildRecipes(recipeExporter: RecipeOutput) {
        buildShapedGymKey(recipeExporter)
        buildExitRope(recipeExporter)

        ElementalTypes.all().forEach { type ->
            buildAttunedGymKey(type, recipeExporter)
            buildAttunedPokeCaches(type, recipeExporter)
        }

        Rarity.entries.forEach { rarity ->
            buildShardRecipes(rarity, recipeExporter)
            buildShapedPokeCaches(rarity, recipeExporter)
        }
    }

    private fun buildShapedPokeCaches(
        rarity: Rarity,
        recipeExporter: RecipeOutput
    ) {
        val cachePair = cacheConfig(rarity)
        val cacheParts = cachePair.first

        if (cacheParts.d == AIR) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cachePair.second, 1)
                .pattern("sss")
                .pattern("s s")
                .pattern("ici")
                .define('s', cacheParts.a)
                .define('i', cacheParts.b)
                .define('c', cacheParts.c)
                .group("multi_bench")
                .unlockedBy(getHasName(cacheParts.a), has(CRAFTING_TABLE))
                .save(recipeExporter, modId("cache_${rarity.serializedName}"))
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cachePair.second, 1)
                .pattern("sss")
                .pattern("sxs")
                .pattern("ici")
                .define('s', cacheParts.a)
                .define('i', cacheParts.b)
                .define('c', cacheParts.c)
                .define('x', cacheParts.d)
                .group("multi_bench")
                .unlockedBy(getHasName(cacheParts.a), has(CRAFTING_TABLE))
                .save(recipeExporter, modId("cache_${rarity.serializedName}"))
        }
    }

    private fun buildShardRecipes(
        rarity: Rarity,
        recipeExporter: RecipeOutput
    ) {
        val shardPair = shardConfig(rarity)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, shardPair.second, 1)
            .pattern("sss")
            .pattern("sss")
            .pattern("sss")
            .define('s', shardPair.first)
            .group("multi_bench")
            .unlockedBy(getHasName(shardPair.first), has(CRAFTING_TABLE))
            .save(recipeExporter, modId("shards_to_block_${rarity.serializedName}"))

        ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, shardPair.first, BLOCK_TO_SHARD_AMOUNT)
            .define(shardPair.second.asItem())
            .group("multi_bench")
            .unlockedBy(getHasName(shardPair.second), has(shardPair.first))
            .save(recipeExporter, modId("block_to_shards_${rarity.serializedName}"))
    }

    private fun buildAttunedPokeCaches(
        type: ElementalType,
        recipeExporter: RecipeOutput
    ) {
        Rarity.entries.forEach { rarity ->
            val cache = cacheForRarity(rarity)
            val attunedCachePair = elementalTypeCacheConfig(cache, type)
            val cacheComponents = DataComponentMap.builder()
                .set(RARITY, rarity)
                .set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)

            ShapelessWithComponentRecipeJsonBuilder(
                RecipeCategory.MISC,
                attunedCachePair.first,
                1
            )
                .define(attunedCachePair.first.asItem())
                .define(attunedCachePair.second.asItem())
                .group("multi_bench")
                .withComponentMap(cacheComponents.build())
                .unlockedBy(
                    getHasName(attunedCachePair.second),
                    has(attunedCachePair.first)
                )
                .save(
                    recipeExporter,
                    modId("cache_${rarity.serializedName}_${type.showdownId}")
                )
        }
    }

    private fun buildAttunedGymKey(
        type: ElementalType,
        recipeExporter: RecipeOutput
    ) {
        try {
            val pair = elementalTypeKeyConfig(type)

            val keyComponents = DataComponentMap.builder()
                .set(RARITY, Rarity.RARE)
                .set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
                .define(GYM_KEY)
                .define(pair.second)
                .group("multi_bench")
                .withComponentMap(keyComponents.build())
                .unlockedBy(
                    getHasName(pair.second),
                    has(pair.first)
                )
                .save(recipeExporter, modId("gym_key_${type.showdownId}"))
        } catch (_: NotImplementedError) {
            debug("No keys found for $type")
        }
    }

    private fun buildExitRope(recipeExporter: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RadGymsItems.EXIT_ROPE, 1)
            .pattern("l")
            .pattern("b")
            .define('l', LEAD)
            .define('b', BINDING_BAND)
            .group("multi_bench")
            .unlockedBy(
                getHasName(BINDING_BAND),
                has(BINDING_BAND)
            )
            .save(recipeExporter)
    }

    private fun buildShapedGymKey(recipeExporter: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GYM_KEY, 1)
            .pattern(" g")
            .pattern("b ")
            .define('g', GOLD_INGOT)
            .define('b', POKE_BALL.item())
            .group("multi_bench")
            .unlockedBy(
                getHasName(POKE_BALL.item()),
                has(CRAFTING_TABLE)
            )
            .save(recipeExporter)
    }

    private fun cacheForRarity(rarity: Rarity): PokeCache {
        return when (rarity) {
            Rarity.COMMON -> CACHE_COMMON
            Rarity.UNCOMMON -> CACHE_UNCOMMON
            Rarity.RARE -> CACHE_RARE
            Rarity.EPIC -> CACHE_EPIC
        }
    }

    @Suppress("CyclomaticComplexMethod")
    @Throws(NotImplementedError::class)
    private fun elementalTypeKeyConfig(type: ElementalType): Pair<GymKey, CobblemonItem> = when (type) {
        BUG -> Pair(GYM_KEY, BUG_GEM)
        DARK -> Pair(GYM_KEY, DARK_GEM)
        DRAGON -> Pair(GYM_KEY, DRAGON_GEM)
        ELECTRIC -> Pair(GYM_KEY, ELECTRIC_GEM)
        FAIRY -> Pair(GYM_KEY, FAIRY_GEM)
        FIGHTING -> Pair(GYM_KEY, FIGHTING_GEM)
        FIRE -> Pair(GYM_KEY, FIRE_GEM)
        FLYING -> Pair(GYM_KEY, FLYING_GEM)
        GHOST -> Pair(GYM_KEY, GHOST_GEM)
        GRASS -> Pair(GYM_KEY, GRASS_GEM)
        GROUND -> Pair(GYM_KEY, GROUND_GEM)
        ICE -> Pair(GYM_KEY, ICE_GEM)
        NORMAL -> Pair(GYM_KEY, NORMAL_GEM)
        POISON -> Pair(GYM_KEY, POISON_GEM)
        PSYCHIC -> Pair(GYM_KEY, PSYCHIC_GEM)
        ROCK -> Pair(GYM_KEY, ROCK_GEM)
        STEEL -> Pair(GYM_KEY, STEEL_GEM)
        WATER -> Pair(GYM_KEY, WATER_GEM)
        else -> {
            throw NotImplementedError("No keys found for $type")
        }
    }

    @Suppress("CyclomaticComplexMethod")
    private fun elementalTypeCacheConfig(cache: PokeCache, type: ElementalType): Pair<PokeCache, CobblemonItem> =
        when (type) {
            BUG -> Pair(cache, BUG_GEM)
            DARK -> Pair(cache, DARK_GEM)
            DRAGON -> Pair(cache, DRAGON_GEM)
            ELECTRIC -> Pair(cache, ELECTRIC_GEM)
            FAIRY -> Pair(cache, FAIRY_GEM)
            FIGHTING -> Pair(cache, FIGHTING_GEM)
            FIRE -> Pair(cache, FIRE_GEM)
            FLYING -> Pair(cache, FLYING_GEM)
            GHOST -> Pair(cache, GHOST_GEM)
            GRASS -> Pair(cache, GRASS_GEM)
            GROUND -> Pair(cache, GROUND_GEM)
            ICE -> Pair(cache, ICE_GEM)
            NORMAL -> Pair(cache, NORMAL_GEM)
            POISON -> Pair(cache, POISON_GEM)
            PSYCHIC -> Pair(cache, PSYCHIC_GEM)
            ROCK -> Pair(cache, ROCK_GEM)
            STEEL -> Pair(cache, STEEL_GEM)
            WATER -> Pair(cache, WATER_GEM)

            else -> {
                debug("No caches found for $type")
                Pair(cache, NORMAL_GEM)
            }
        }

    private fun shardConfig(rarity: Rarity): Pair<PokeShardBase, Item> {
        return when (rarity) {
            Rarity.COMMON -> Pair(SHARD_COMMON, SHARD_BLOCK_COMMON)
            Rarity.UNCOMMON -> Pair(SHARD_UNCOMMON, SHARD_BLOCK_UNCOMMON)
            Rarity.RARE -> Pair(SHARD_RARE, SHARD_BLOCK_RARE)
            Rarity.EPIC -> Pair(SHARD_EPIC, SHARD_BLOCK_EPIC)
        }
    }

    private fun cacheConfig(rarity: Rarity): Pair<Quartet<PokeShardBase, Item, Item, Item>, Item> = when (rarity) {
        Rarity.COMMON -> {
            Pair(
                Quartet(
                    SHARD_COMMON,
                    IRON_INGOT,
                    LUXURY_BALL.item(),
                    AIR
                ),
                CACHE_COMMON
            )
        }

        Rarity.UNCOMMON -> {
            Pair(
                Quartet(
                    SHARD_UNCOMMON,
                    GOLD_INGOT,
                    LUXURY_BALL.item(),
                    AIR
                ),
                CACHE_UNCOMMON
            )
        }

        Rarity.RARE -> {
            Pair(
                Quartet(
                    SHARD_RARE,
                    DIAMOND,
                    LUXURY_BALL.item(),
                    AIR
                ),
                CACHE_RARE
            )
        }

        Rarity.EPIC -> {
            Pair(
                Quartet(
                    SHARD_EPIC,
                    NETHERITE_INGOT,
                    LUXURY_BALL.item(),
                    ECHO_SHARD
                ),
                CACHE_EPIC
            )
        }
    }
}
