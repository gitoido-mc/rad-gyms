package lol.gito.radgyms.datagen.recipe

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.item.CobblemonItem
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.datagen.json.builder.ShapelessWithComponentRecipeJsonBuilder
import lol.gito.radgyms.item.DataComponentManager
import lol.gito.radgyms.item.GymKey
import lol.gito.radgyms.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Rarity
import java.util.concurrent.CompletableFuture

class RecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generate(recipeExporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.GYM_KEY, 1)
            .pattern("  g")
            .pattern(" b ")
            .pattern("   ")
            .input('g', Items.GOLD_INGOT)
            .input('b', PokeBalls.POKE_BALL.item())
            .group("multi_bench")
            .criterion(hasItem(Items.CRAFTING_TABLE), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
            .offerTo(recipeExporter)


        for (type in ElementalTypes.all()) {
            val pair = elementalTypeKeys(type)

            val components = ComponentMap.builder();
            components.add(DataComponentTypes.RARITY, Rarity.EPIC)
            components.add(DataComponentManager.GYM_TYPE_COMPONENT, type.name)

            ShapelessWithComponentRecipeJsonBuilder(RecipeCategory.MISC, pair.first)
                .input(ItemRegistry.GYM_KEY)
                .input(pair.second)
                .group("multi_bench")
                .withComponentMap(components.build())
                .criterion(hasItem(pair.second), RecipeProvider.conditionsFromItem(pair.first))
                .offerTo(recipeExporter, "${type.name}_gym_key")
        }
    }

    private fun elementalTypeKeys(type: ElementalType): Pair<GymKey, CobblemonItem> {
        return when (type) {
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
                RadGyms.LOGGER.info("No keys found for $type")
                Pair(ItemRegistry.GYM_KEY, CobblemonItems.NORMAL_GEM)
            }
        }
    }
}