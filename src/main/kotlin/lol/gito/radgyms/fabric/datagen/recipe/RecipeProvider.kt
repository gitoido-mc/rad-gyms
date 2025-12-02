/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.datagen.recipe

//class RecipeProvider(
//    output: FabricDataOutput,
//    registriesFuture: CompletableFuture<HolderLookup.Provider>
//) : FabricRecipeProvider(output, registriesFuture) {
//    override fun buildRecipes(recipeExporter: RecipeOutput) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RadGymsItems.GYM_KEY, 1)
//            .pattern(" g")
//            .pattern("b ")
//            .define('g', Items.GOLD_INGOT)
//            .define('b', PokeBalls.POKE_BALL.item())
//            .group("multi_bench")
//            .unlockedBy(hasItem(PokeBalls.POKE_BALL.item()), conditionsFromItem(Items.CRAFTING_TABLE))
//            .offerTo(recipeExporter)
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RadGymsItems.EXIT_ROPE, 1)
//            .pattern("l")
//            .pattern("b")
//            .define('l', Items.LEAD)
//            .define('b', CobblemonItems.BINDING_BAND)
//            .group("multi_bench")
//            .criterion(
//                hasItem(CobblemonItems.BINDING_BAND),
//                conditionsFromItem(CobblemonItems.BINDING_BAND)
//            )
//            .offerTo(recipeExporter)
//
//        ElementalTypes.all().forEach { type ->
//            val pair = elementalTypeKeyConfig(type)
//
//            val keyComponents = DataComponentMap.builder()
//                .add(DataComponents.RARITY, Rarity.RARE)
//                .add(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)
//
//            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
//                .define(RadGymsItems.GYM_KEY)
//                .define(pair.second)
//                .group("multi_bench")
//                .withComponentMap(keyComponents.build())
//                .criterion(hasItem(pair.second), conditionsFromItem(pair.first))
//                .offerTo(recipeExporter, "gym_key_${type.showdownId}")
//
//            Rarity.entries.forEach { rarity ->
//                val cache = cacheForRarity(rarity)
//                val attunedCachePair = elementalTypeCacheConfig(cache, type)
//                val cacheComponents = ComponentMap.builder()
//                    .add(DataComponentTypes.RARITY, rarity)
//                    .add(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)
//
//                ShapelessWithComponentRecipeJsonBuilder(
//                    RecipeCategory.MISC,
//                    attunedCachePair.first,
//                    1
//                )
//                    .input(attunedCachePair.first.asItem())
//                    .input(attunedCachePair.second.asItem())
//                    .group("multi_bench")
//                    .withComponentMap(cacheComponents.build())
//                    .criterion(
//                        hasItem(attunedCachePair.second),
//                        conditionsFromItem(attunedCachePair.first)
//                    )
//                    .offerTo(
//                        recipeExporter,
//                        "cache_${rarity.name.replace("minecraft:", "").lowercase()}_${type.showdownId}"
//                    )
//            }
//        }
//
//        Rarity.entries.forEach { rarity ->
//            val shardPair = shardConfig(rarity)
//            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, shardPair.second, 1)
//                .pattern("sss")
//                .pattern("sss")
//                .pattern("sss")
//                .input('s', shardPair.first)
//                .group("multi_bench")
//                .criterion(hasItem(shardPair.first), conditionsFromItem(Items.CRAFTING_TABLE))
//                .offerTo(recipeExporter, "shards_to_block_${rarity.name.replace("minecraft:", "").lowercase()}")
//
//            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, shardPair.first, 9)
//                .input(shardPair.second.asItem())
//                .group("multi_bench")
//                .criterion(hasItem(shardPair.second), conditionsFromItem(shardPair.first))
//                .offerTo(recipeExporter, "block_to_shards_${rarity.name.replace("minecraft:", "").lowercase()}")
//
//
//            val cachePair = cacheConfig(rarity)
//            if (cachePair != null) {
//                val cacheParts = cachePair.first
//
//                if (cacheParts.d == null) {
//                    ShapedRecipeBuilder.create(RecipeCategory.MISC, cachePair.second, 1)
//                        .pattern("sss")
//                        .pattern("s s")
//                        .pattern("ici")
//                        .input('s', cacheParts.a)
//                        .input('i', cacheParts.b)
//                        .input('c', cacheParts.c)
//                        .group("multi_bench")
//                        .criterion(hasItem(cacheParts.a), conditionsFromItem(Items.CRAFTING_TABLE))
//                        .offerTo(recipeExporter, "cache_${rarity.name.replace("minecraft:", "").lowercase()}")
//                } else {
//                    ShapedRecipeBuilder.create(RecipeCategory.MISC, cachePair.second, 1)
//                        .pattern("sss")
//                        .pattern("sxs")
//                        .pattern("ici")
//                        .input('s', cacheParts.a)
//                        .input('i', cacheParts.b)
//                        .input('c', cacheParts.c)
//                        .input('x', cacheParts.d)
//                        .group("multi_bench")
//                        .criterion(hasItem(cacheParts.a), conditionsFromItem(Items.CRAFTING_TABLE))
//                        .offerTo(recipeExporter, "cache_${rarity.name.replace("minecraft:", "").lowercase()}")
//                }
//            }
//        }
//    }
//
//    private fun cacheForRarity(rarity: Rarity): PokeCache {
//        return when (rarity) {
//            Rarity.COMMON -> RadGymsItems.CACHE_COMMON
//            Rarity.UNCOMMON -> RadGymsItems.CACHE_UNCOMMON
//            Rarity.RARE -> RadGymsItems.CACHE_RARE
//            Rarity.EPIC -> RadGymsItems.CACHE_EPIC
//        }
//    }
//
//    private fun elementalTypeKeyConfig(type: ElementalType): Pair<GymKey, CobblemonItem> = when (type) {
//        ElementalTypes.BUG -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.BUG_GEM)
//        ElementalTypes.DARK -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.DARK_GEM)
//        ElementalTypes.DRAGON -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.DRAGON_GEM)
//        ElementalTypes.ELECTRIC -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ELECTRIC_GEM)
//        ElementalTypes.FAIRY -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FAIRY_GEM)
//        ElementalTypes.FIGHTING -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FIGHTING_GEM)
//        ElementalTypes.FIRE -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FIRE_GEM)
//        ElementalTypes.FLYING -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.FLYING_GEM)
//        ElementalTypes.GHOST -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GHOST_GEM)
//        ElementalTypes.GRASS -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GRASS_GEM)
//        ElementalTypes.GROUND -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.GROUND_GEM)
//        ElementalTypes.ICE -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ICE_GEM)
//        ElementalTypes.NORMAL -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.NORMAL_GEM)
//        ElementalTypes.POISON -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.POISON_GEM)
//        ElementalTypes.PSYCHIC -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.PSYCHIC_GEM)
//        ElementalTypes.ROCK -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.ROCK_GEM)
//        ElementalTypes.STEEL -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.STEEL_GEM)
//        ElementalTypes.WATER -> Pair(RadGymsItems.GYM_KEY, CobblemonItems.WATER_GEM)
//        else -> {
//            debug("No keys found for $type")
//            throw NotImplementedError("No keys found for $type")
//        }
//    }
//
//    private fun elementalTypeCacheConfig(cache: PokeCache, type: ElementalType): Pair<PokeCache, CobblemonItem> =
//        when (type) {
//            ElementalTypes.BUG -> Pair(cache, CobblemonItems.BUG_GEM)
//            ElementalTypes.DARK -> Pair(cache, CobblemonItems.DARK_GEM)
//            ElementalTypes.DRAGON -> Pair(cache, CobblemonItems.DRAGON_GEM)
//            ElementalTypes.ELECTRIC -> Pair(cache, CobblemonItems.ELECTRIC_GEM)
//            ElementalTypes.FAIRY -> Pair(cache, CobblemonItems.FAIRY_GEM)
//            ElementalTypes.FIGHTING -> Pair(cache, CobblemonItems.FIGHTING_GEM)
//            ElementalTypes.FIRE -> Pair(cache, CobblemonItems.FIRE_GEM)
//            ElementalTypes.FLYING -> Pair(cache, CobblemonItems.FLYING_GEM)
//            ElementalTypes.GHOST -> Pair(cache, CobblemonItems.GHOST_GEM)
//            ElementalTypes.GRASS -> Pair(cache, CobblemonItems.GRASS_GEM)
//            ElementalTypes.GROUND -> Pair(cache, CobblemonItems.GROUND_GEM)
//            ElementalTypes.ICE -> Pair(cache, CobblemonItems.ICE_GEM)
//            ElementalTypes.NORMAL -> Pair(cache, CobblemonItems.NORMAL_GEM)
//            ElementalTypes.POISON -> Pair(cache, CobblemonItems.POISON_GEM)
//            ElementalTypes.PSYCHIC -> Pair(cache, CobblemonItems.PSYCHIC_GEM)
//            ElementalTypes.ROCK -> Pair(cache, CobblemonItems.ROCK_GEM)
//            ElementalTypes.STEEL -> Pair(cache, CobblemonItems.STEEL_GEM)
//            ElementalTypes.WATER -> Pair(cache, CobblemonItems.WATER_GEM)
//
//            else -> {
//                debug("No caches found for $type")
//                Pair(cache, CobblemonItems.NORMAL_GEM)
//            }
//        }
//
//    private fun shardConfig(rarity: Rarity): Pair<PokeShardBase, Item> {
//        return when (rarity) {
//            Rarity.COMMON -> Pair(RadGymsItems.SHARD_COMMON, RadGymsBlocks.SHARD_BLOCK_COMMON.asItem())
//            Rarity.UNCOMMON -> Pair(RadGymsItems.SHARD_UNCOMMON, RadGymsBlocks.SHARD_BLOCK_UNCOMMON.asItem())
//            Rarity.RARE -> Pair(RadGymsItems.SHARD_RARE, RadGymsBlocks.SHARD_BLOCK_RARE.asItem())
//            Rarity.EPIC -> Pair(RadGymsItems.SHARD_EPIC, RadGymsBlocks.SHARD_BLOCK_EPIC.asItem())
//        }
//    }
//
//    private fun cacheConfig(rarity: Rarity): Pair<Quartet<PokeShardBase, Item, Item, Item?>, Item>? {
//        return when (rarity) {
//            Rarity.COMMON -> {
//                Pair(
//                    Quartet(
//                        RadGymsItems.SHARD_COMMON,
//                        Items.IRON_INGOT,
//                        PokeBalls.LUXURY_BALL.item(),
//                        null
//                    ),
//                    RadGymsItems.CACHE_COMMON
//                )
//            }
//
//            Rarity.UNCOMMON -> {
//                Pair(
//                    Quartet(
//                        RadGymsItems.SHARD_UNCOMMON,
//                        Items.GOLD_INGOT,
//                        PokeBalls.LUXURY_BALL.item(),
//                        null
//                    ),
//                    RadGymsItems.CACHE_UNCOMMON
//                )
//            }
//
//            Rarity.RARE -> {
//                Pair(
//                    Quartet(
//                        RadGymsItems.SHARD_RARE,
//                        Items.DIAMOND,
//                        PokeBalls.LUXURY_BALL.item(),
//                        null
//                    ),
//                    RadGymsItems.CACHE_RARE
//                )
//            }
//
//            Rarity.EPIC -> {
//                Pair(
//                    Quartet(
//                        RadGymsItems.SHARD_EPIC,
//                        Items.NETHERITE_INGOT,
//                        PokeBalls.LUXURY_BALL.item(),
//                        Items.ECHO_SHARD
//                    ),
//                    RadGymsItems.CACHE_EPIC
//                )
//            }
//        }
//    }
//}
