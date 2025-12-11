/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.recipe

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.item.GymKey
import lol.gito.radgyms.common.item.PokeCache
import lol.gito.radgyms.common.item.PokeShardBase
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.fabric.datagen.json.builder.ShapelessWithComponentRecipeJsonBuilder
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import oshi.util.tuples.Quartet
import java.util.concurrent.CompletableFuture

class RecipeProvider(
    output: FabricDataOutput,
    lookup: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, lookup) {
    override fun buildRecipes(recipeExporter: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RadGymsItems.GYM_KEY, 1)
            .pattern(" g")
            .pattern("b ")
            .define('g', Items.GOLD_INGOT)
            .define('b', PokeBalls.POKE_BALL.item())
            .group("multi_bench")
            .unlockedBy(
                getHasName(PokeBalls.POKE_BALL.item()),
                has(Items.CRAFTING_TABLE)
            )
            .save(recipeExporter)

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RadGymsItems.EXIT_ROPE, 1)
            .pattern("l")
            .pattern("b")
            .define('l', Items.LEAD)
            .define('b', CobblemonItems.BINDING_BAND)
            .group("multi_bench")
            .unlockedBy(
                getHasName(CobblemonItems.BINDING_BAND),
                has(CobblemonItems.BINDING_BAND)
            )
            .save(recipeExporter)

        ElementalTypes.all().forEach { type ->
            val pair = elementalTypeKeyConfig(type)

            val keyComponents = DataComponentMap.builder()
                .set(DataComponents.RARITY, Rarity.RARE)
                .set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
                .define(RadGymsItems.GYM_KEY)
                .define(pair.second)
                .group("multi_bench")
                .withComponentMap(keyComponents.build())
                .unlockedBy(
                    getHasName(pair.second),
                    has(pair.first)
                )
                .save(recipeExporter, modId("gym_key_${type.showdownId}"))

            Rarity.entries.forEach { rarity ->
                val cache = cacheForRarity(rarity)
                val attunedCachePair = elementalTypeCacheConfig(cache, type)
                val cacheComponents = DataComponentMap.builder()
                    .set(DataComponents.RARITY, rarity)
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

        Rarity.entries.forEach { rarity ->
            val shardPair = shardConfig(rarity)
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, shardPair.second, 1)
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .define('s', shardPair.first)
                .group("multi_bench")
                .unlockedBy(getHasName(shardPair.first), has(Items.CRAFTING_TABLE))
                .save(recipeExporter, modId("shards_to_block_${rarity.serializedName}"))

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, shardPair.first, 9)
                .define(shardPair.second.asItem())
                .group("multi_bench")
                .unlockedBy(getHasName(shardPair.second), has(shardPair.first))
                .save(recipeExporter, modId("block_to_shards_${rarity.serializedName}"))


            val cachePair = cacheConfig(rarity)
            val cacheParts = cachePair.first

            if (cacheParts.d == Items.AIR) {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cachePair.second, 1)
                    .pattern("sss")
                    .pattern("s s")
                    .pattern("ici")
                    .define('s', cacheParts.a)
                    .define('i', cacheParts.b)
                    .define('c', cacheParts.c)
                    .group("multi_bench")
                    .unlockedBy(getHasName(cacheParts.a), has(Items.CRAFTING_TABLE))
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
                    .unlockedBy(getHasName(cacheParts.a), has(Items.CRAFTING_TABLE))
                    .save(recipeExporter, modId("cache_${rarity.serializedName}"))
            }

        }
    }

    private fun cacheForRarity(rarity: Rarity): PokeCache {
        return when (rarity) {
            Rarity.COMMON -> RadGymsItems.CACHE_COMMON
            Rarity.UNCOMMON -> RadGymsItems.CACHE_UNCOMMON
            Rarity.RARE -> RadGymsItems.CACHE_RARE
            Rarity.EPIC -> RadGymsItems.CACHE_EPIC
        }
    }

    private fun elementalTypeKeyConfig(type: ElementalType): Pair<GymKey, CobblemonItem> = when (type) {
        ElementalTypes.BUG -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.BUG_GEM)
        ElementalTypes.DARK -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.DARK_GEM)
        ElementalTypes.DRAGON -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.DRAGON_GEM)
        ElementalTypes.ELECTRIC -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ELECTRIC_GEM)
        ElementalTypes.FAIRY -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FAIRY_GEM)
        ElementalTypes.FIGHTING -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FIGHTING_GEM)
        ElementalTypes.FIRE -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FIRE_GEM)
        ElementalTypes.FLYING -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FLYING_GEM)
        ElementalTypes.GHOST -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GHOST_GEM)
        ElementalTypes.GRASS -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GRASS_GEM)
        ElementalTypes.GROUND -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GROUND_GEM)
        ElementalTypes.ICE -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ICE_GEM)
        ElementalTypes.NORMAL -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.NORMAL_GEM)
        ElementalTypes.POISON -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.POISON_GEM)
        ElementalTypes.PSYCHIC -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.PSYCHIC_GEM)
        ElementalTypes.ROCK -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ROCK_GEM)
        ElementalTypes.STEEL -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.STEEL_GEM)
        ElementalTypes.WATER -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.WATER_GEM)
        else -> {
            debug("No keys found for $type")
            throw NotImplementedError("No keys found for $type")
        }
    }

    private fun elementalTypeCacheConfig(cache: PokeCache, type: ElementalType): Pair<PokeCache, CobblemonItem> =
        when (type) {
            ElementalTypes.BUG -> Pair(cache, CobblemonItems.BUG_GEM)
            ElementalTypes.DARK -> Pair(cache, CobblemonItems.DARK_GEM)
            ElementalTypes.DRAGON -> Pair(cache, CobblemonItems.DRAGON_GEM)
            ElementalTypes.ELECTRIC -> Pair(cache, CobblemonItems.ELECTRIC_GEM)
            ElementalTypes.FAIRY -> Pair(cache, CobblemonItems.FAIRY_GEM)
            ElementalTypes.FIGHTING -> Pair(cache, CobblemonItems.FIGHTING_GEM)
            ElementalTypes.FIRE -> Pair(cache, CobblemonItems.FIRE_GEM)
            ElementalTypes.FLYING -> Pair(cache, CobblemonItems.FLYING_GEM)
            ElementalTypes.GHOST -> Pair(cache, CobblemonItems.GHOST_GEM)
            ElementalTypes.GRASS -> Pair(cache, CobblemonItems.GRASS_GEM)
            ElementalTypes.GROUND -> Pair(cache, CobblemonItems.GROUND_GEM)
            ElementalTypes.ICE -> Pair(cache, CobblemonItems.ICE_GEM)
            ElementalTypes.NORMAL -> Pair(cache, CobblemonItems.NORMAL_GEM)
            ElementalTypes.POISON -> Pair(cache, CobblemonItems.POISON_GEM)
            ElementalTypes.PSYCHIC -> Pair(cache, CobblemonItems.PSYCHIC_GEM)
            ElementalTypes.ROCK -> Pair(cache, CobblemonItems.ROCK_GEM)
            ElementalTypes.STEEL -> Pair(cache, CobblemonItems.STEEL_GEM)
            ElementalTypes.WATER -> Pair(cache, CobblemonItems.WATER_GEM)

            else -> {
                debug("No caches found for $type")
                Pair(cache, CobblemonItems.NORMAL_GEM)
            }
        }

    private fun shardConfig(rarity: Rarity): Pair<PokeShardBase, Item> {
        return when (rarity) {
            Rarity.COMMON -> Pair(RadGymsItems.SHARD_COMMON, RadGymsItems.SHARD_BLOCK_COMMON)
            Rarity.UNCOMMON -> Pair(RadGymsItems.SHARD_UNCOMMON, RadGymsItems.SHARD_BLOCK_UNCOMMON)
            Rarity.RARE -> Pair(RadGymsItems.SHARD_RARE, RadGymsItems.SHARD_BLOCK_RARE)
            Rarity.EPIC -> Pair(RadGymsItems.SHARD_EPIC, RadGymsItems.SHARD_BLOCK_EPIC)
        }
    }

    private fun cacheConfig(rarity: Rarity): Pair<Quartet<PokeShardBase, Item, Item, Item>, Item> = when (rarity) {
        Rarity.COMMON -> {
            Pair(
                Quartet(
                    RadGymsItems.SHARD_COMMON,
                    Items.IRON_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    Items.AIR
                ),
                RadGymsItems.CACHE_COMMON
            )
        }

        Rarity.UNCOMMON -> {
            Pair(
                Quartet(
                    RadGymsItems.SHARD_UNCOMMON,
                    Items.GOLD_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    Items.AIR
                ),
                RadGymsItems.CACHE_UNCOMMON
            )
        }

        Rarity.RARE -> {
            Pair(
                Quartet(
                    RadGymsItems.SHARD_RARE,
                    Items.DIAMOND,
                    PokeBalls.LUXURY_BALL.item(),
                    Items.AIR
                ),
                RadGymsItems.CACHE_RARE
            )
        }

        Rarity.EPIC -> {
            Pair(
                Quartet(
                    RadGymsItems.SHARD_EPIC,
                    Items.NETHERITE_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    Items.ECHO_SHARD
                ),
                RadGymsItems.CACHE_EPIC
            )
        }
    }
}
