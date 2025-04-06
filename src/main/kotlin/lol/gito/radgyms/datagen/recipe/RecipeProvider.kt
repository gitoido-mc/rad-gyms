package lol.gito.radgyms.datagen.recipe

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.datagen.json.builder.ShapelessWithComponentRecipeJsonBuilder
import lol.gito.radgyms.item.GymKey
import lol.gito.radgyms.item.ItemRegistry
import lol.gito.radgyms.item.PokeShardBase
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Rarity
import oshi.util.tuples.Quartet
import java.util.concurrent.CompletableFuture

class RecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generate(recipeExporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.GYM_KEY, 1)
            .pattern(" g")
            .pattern("b ")
            .input('g', Items.GOLD_INGOT)
            .input('b', PokeBalls.POKE_BALL.item())
            .group("multi_bench")
            .criterion(hasItem(PokeBalls.POKE_BALL.item()), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
            .offerTo(recipeExporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.EXIT_ROPE, 1)
            .pattern("l")
            .pattern("b")
            .input('l', Items.LEAD)
            .input('b', CobblemonItems.BINDING_BAND)
            .group("multi_bench")
            .criterion(hasItem(CobblemonItems.BINDING_BAND), RecipeProvider.conditionsFromItem(CobblemonItems.BINDING_BAND))
            .offerTo(recipeExporter)

        ElementalTypes.all().forEach { type ->
            val pair = elementalTypeKeyConfig(type)

            val components = ComponentMap.builder()
                .add(DataComponentTypes.RARITY, Rarity.RARE)
                .add(DataComponentManager.GYM_TYPE_COMPONENT, type.name)

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
                .input(ItemRegistry.GYM_KEY)
                .input(pair.second)
                .group("multi_bench")
                .withComponentMap(components.build())
                .criterion(hasItem(pair.second), RecipeProvider.conditionsFromItem(pair.first))
                .offerTo(recipeExporter, "gym_key_${type.name}")
        }

        Rarity.entries.forEach { rarity ->
            val shardPair = shardConfig(rarity)
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, shardPair.second, 1)
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .input('s', shardPair.first)
                .group("multi_bench")
                .criterion(hasItem(shardPair.first), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
                .offerTo(recipeExporter, "shards_to_block_${rarity.name.replace("minecraft:", "").lowercase()}")

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, shardPair.first, 9)
                .input(shardPair.second.asItem())
                .group("multi_bench")
                .criterion(hasItem(shardPair.second), RecipeProvider.conditionsFromItem(shardPair.first))
                .offerTo(recipeExporter, "block_to_shards_${rarity.name.replace("minecraft:", "").lowercase()}")

            val cachePair = cacheConfig(rarity)
            val cacheParts = cachePair.first

            if (cacheParts.d == null) {
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, cachePair.second, 1)
                    .pattern("sss")
                    .pattern("s s")
                    .pattern("ici")
                    .input('s', cacheParts.a)
                    .input('i', cacheParts.b)
                    .input('c', cacheParts.c)
                    .group("multi_bench")
                    .criterion(hasItem(cacheParts.a), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
                    .offerTo(recipeExporter, "cache_${rarity.name.replace("minecraft:", "").lowercase()}")
            } else {
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, cachePair.second, 1)
                    .pattern("sss")
                    .pattern("sxs")
                    .pattern("ici")
                    .input('s', cacheParts.a)
                    .input('i', cacheParts.b)
                    .input('c', cacheParts.c)
                    .input('x', cacheParts.d)
                    .group("multi_bench")
                    .criterion(hasItem(cacheParts.a), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
                    .offerTo(recipeExporter, "cache_${rarity.name.replace("minecraft:", "").lowercase()}")
            }
        }
    }

    private fun elementalTypeKeyConfig(type: ElementalType): Pair<GymKey, CobblemonItem> = when (type) {
        ElementalTypes.BUG -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.BUG_GEM)
        ElementalTypes.DARK -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.DARK_GEM)
        ElementalTypes.DRAGON -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.DRAGON_GEM)
        ElementalTypes.ELECTRIC -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.ELECTRIC_GEM)
        ElementalTypes.FAIRY -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.FAIRY_GEM)
        ElementalTypes.FIGHTING -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.FIGHTING_GEM)
        ElementalTypes.FIRE -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.FIRE_GEM)
        ElementalTypes.FLYING -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.FLYING_GEM)
        ElementalTypes.GHOST -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.GHOST_GEM)
        ElementalTypes.GRASS -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.GRASS_GEM)
        ElementalTypes.GROUND -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.GROUND_GEM)
        ElementalTypes.ICE -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.ICE_GEM)
        ElementalTypes.NORMAL -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.NORMAL_GEM)
        ElementalTypes.POISON -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.POISON_GEM)
        ElementalTypes.PSYCHIC -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.PSYCHIC_GEM)
        ElementalTypes.ROCK -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.ROCK_GEM)
        ElementalTypes.STEEL -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.STEEL_GEM)
        ElementalTypes.WATER -> Pair(ItemRegistry.GYM_KEY, CobblemonItems.WATER_GEM)

        else -> {
            debug("No keys found for $type")
            Pair(ItemRegistry.GYM_KEY, CobblemonItems.NORMAL_GEM)
        }
    }

    private fun shardConfig(rarity: Rarity): Pair<PokeShardBase, Item> = when (rarity) {
        Rarity.COMMON -> Pair(ItemRegistry.SHARD_COMMON, BlockRegistry.SHARD_BLOCK_COMMON.asItem())
        Rarity.UNCOMMON -> Pair(ItemRegistry.SHARD_UNCOMMON, BlockRegistry.SHARD_BLOCK_UNCOMMON.asItem())
        Rarity.RARE -> Pair(ItemRegistry.SHARD_RARE, BlockRegistry.SHARD_BLOCK_RARE.asItem())
        Rarity.EPIC -> Pair(ItemRegistry.SHARD_EPIC, BlockRegistry.SHARD_BLOCK_EPIC.asItem())
    }

    private fun cacheConfig(rarity: Rarity): Pair<Quartet<PokeShardBase, Item, Item, Item?>, Item> = when(rarity) {
        Rarity.COMMON -> {
            Pair(
                Quartet(
                    ItemRegistry.SHARD_COMMON,
                    Items.IRON_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    null
                ),
                ItemRegistry.CACHE_COMMON
            )
        }
        Rarity.UNCOMMON -> {
            Pair(
                Quartet(
                    ItemRegistry.SHARD_UNCOMMON,
                    Items.GOLD_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    null
                ),
                ItemRegistry.CACHE_UNCOMMON
            )
        }
        Rarity.RARE -> {
            Pair(
                Quartet(
                    ItemRegistry.SHARD_RARE,
                    Items.DIAMOND,
                    PokeBalls.LUXURY_BALL.item(),
                    null
                ),
                ItemRegistry.CACHE_RARE
            )
        }
        Rarity.EPIC -> {
            Pair(
                Quartet(
                    ItemRegistry.SHARD_EPIC,
                    Items.NETHERITE_INGOT,
                    PokeBalls.LUXURY_BALL.item(),
                    Items.ECHO_SHARD
                ),
                ItemRegistry.CACHE_EPIC
            )
        }
    }
}
