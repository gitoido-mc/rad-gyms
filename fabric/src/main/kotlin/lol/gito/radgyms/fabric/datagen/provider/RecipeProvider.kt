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
import com.cobblemon.mod.common.item.PokeBallItem
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

class RecipeProvider(output: FabricDataOutput, lookup: CompletableFuture<HolderLookup.Provider>) :
    FabricRecipeProvider(output, lookup) {

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

    private fun buildShapedPokeCaches(rarity: Rarity, recipeExporter: RecipeOutput) {
        val cachePair = cacheConfig(rarity)
        val cacheParts = cachePair.first

        if (cacheParts.d == AIR) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, cachePair.second, 1)
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
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, cachePair.second, 1)
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

    private fun buildShardRecipes(rarity: Rarity, recipeExporter: RecipeOutput) {
        val shardPair = shardConfig(rarity)
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, shardPair.second, 1)
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

    private fun buildAttunedPokeCaches(type: ElementalType, recipeExporter: RecipeOutput) = Rarity.entries
        .forEach { rarity ->
            val cache = cacheForRarity(rarity)
            val attunedCachePair = elementalTypeCacheConfig(cache, type)
            val cacheComponents =
                DataComponentMap
                    .builder()
                    .set(RARITY, rarity)
                    .set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, attunedCachePair.first, 1)
                .define(attunedCachePair.first.asItem())
                .define(attunedCachePair.second.asItem())
                .group("multi_bench")
                .withComponentMap(cacheComponents.build())
                .unlockedBy(getHasName(attunedCachePair.second), has(attunedCachePair.first))
                .save(recipeExporter, modId("cache_${rarity.serializedName}_${type.showdownId}"))
        }

    private fun buildAttunedGymKey(type: ElementalType, recipeExporter: RecipeOutput) = try {
        val pair = elementalTypeKeyConfig(type)

        val keyComponents =
            DataComponentMap
                .builder()
                .set(RARITY, Rarity.RARE)
                .set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)

        ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
            .define(GYM_KEY)
            .define(pair.second)
            .group("multi_bench")
            .withComponentMap(keyComponents.build())
            .unlockedBy(getHasName(pair.second), has(pair.first))
            .save(recipeExporter, modId("gym_key_${type.showdownId}"))
    } catch (_: NotImplementedError) {
        debug("No keys found for $type")
    }

    private fun buildExitRope(recipeExporter: RecipeOutput) = ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, RadGymsItems.EXIT_ROPE, 1)
        .pattern("l")
        .pattern("b")
        .define('l', LEAD)
        .define('b', BINDING_BAND)
        .group("multi_bench")
        .unlockedBy(getHasName(BINDING_BAND), has(BINDING_BAND))
        .save(recipeExporter)

    private fun buildShapedGymKey(recipeExporter: RecipeOutput) = ShapedRecipeBuilder
        .shaped(RecipeCategory.MISC, GYM_KEY, 1)
        .pattern(" g")
        .pattern("b ")
        .define('g', GOLD_INGOT)
        .define('b', POKE_BALL.item())
        .group("multi_bench")
        .unlockedBy(getHasName(POKE_BALL.item()), has(CRAFTING_TABLE))
        .save(recipeExporter)

    private fun cacheForRarity(rarity: Rarity): PokeCache = when (rarity) {
        Rarity.COMMON -> CACHE_COMMON
        Rarity.UNCOMMON -> CACHE_UNCOMMON
        Rarity.RARE -> CACHE_RARE
        Rarity.EPIC -> CACHE_EPIC
    }

    @Suppress("CyclomaticComplexMethod")
    @Throws(NotImplementedError::class)
    private fun elementalTypeKeyConfig(type: ElementalType): Pair<GymKey, CobblemonItem> = when (type) {
        BUG -> GYM_KEY to BUG_GEM
        DARK -> GYM_KEY to DARK_GEM
        DRAGON -> GYM_KEY to DRAGON_GEM
        ELECTRIC -> GYM_KEY to ELECTRIC_GEM
        FAIRY -> GYM_KEY to FAIRY_GEM
        FIGHTING -> GYM_KEY to FIGHTING_GEM
        FIRE -> GYM_KEY to FIRE_GEM
        FLYING -> GYM_KEY to FLYING_GEM
        GHOST -> GYM_KEY to GHOST_GEM
        GRASS -> GYM_KEY to GRASS_GEM
        GROUND -> GYM_KEY to GROUND_GEM
        ICE -> GYM_KEY to ICE_GEM
        NORMAL -> GYM_KEY to NORMAL_GEM
        POISON -> GYM_KEY to POISON_GEM
        PSYCHIC -> GYM_KEY to PSYCHIC_GEM
        ROCK -> GYM_KEY to ROCK_GEM
        STEEL -> GYM_KEY to STEEL_GEM
        WATER -> GYM_KEY to WATER_GEM
        else -> {
            throw NotImplementedError("No keys found for $type")
        }
    }

    @Suppress("CyclomaticComplexMethod")
    private fun elementalTypeCacheConfig(cache: PokeCache, type: ElementalType): Pair<PokeCache, CobblemonItem> =
        when (type) {
            BUG -> cache to BUG_GEM
            DARK -> cache to DARK_GEM
            DRAGON -> cache to DRAGON_GEM
            ELECTRIC -> cache to ELECTRIC_GEM
            FAIRY -> cache to FAIRY_GEM
            FIGHTING -> cache to FIGHTING_GEM
            FIRE -> cache to FIRE_GEM
            FLYING -> cache to FLYING_GEM
            GHOST -> cache to GHOST_GEM
            GRASS -> cache to GRASS_GEM
            GROUND -> cache to GROUND_GEM
            ICE -> cache to ICE_GEM
            NORMAL -> cache to NORMAL_GEM
            POISON -> cache to POISON_GEM
            PSYCHIC -> cache to PSYCHIC_GEM
            ROCK -> cache to ROCK_GEM
            STEEL -> cache to STEEL_GEM
            WATER -> cache to WATER_GEM

            else -> {
                debug("No caches found for $type")
                Pair(cache, NORMAL_GEM)
            }
        }

    private fun shardConfig(rarity: Rarity): Pair<PokeShardBase, Item> = when (rarity) {
        Rarity.COMMON -> SHARD_COMMON to SHARD_BLOCK_COMMON
        Rarity.UNCOMMON -> SHARD_UNCOMMON to SHARD_BLOCK_UNCOMMON
        Rarity.RARE -> SHARD_RARE to SHARD_BLOCK_RARE
        Rarity.EPIC -> SHARD_EPIC to SHARD_BLOCK_EPIC
    }

    private fun cacheConfig(rarity: Rarity): Pair<Quartet<out PokeShardBase, Item, PokeBallItem, Item>, PokeCache> =
        when (rarity) {
            Rarity.COMMON -> Quartet(SHARD_COMMON, IRON_INGOT, LUXURY_BALL.item(), AIR) to CACHE_COMMON
            Rarity.UNCOMMON -> Quartet(SHARD_UNCOMMON, GOLD_INGOT, LUXURY_BALL.item(), AIR) to CACHE_UNCOMMON
            Rarity.RARE -> Quartet(SHARD_RARE, DIAMOND, LUXURY_BALL.item(), AIR) to CACHE_RARE
            Rarity.EPIC -> Quartet(SHARD_EPIC, NETHERITE_INGOT, LUXURY_BALL.item(), ECHO_SHARD) to CACHE_EPIC
        }

    companion object {
        const val BLOCK_TO_SHARD_AMOUNT = 9
    }
}
