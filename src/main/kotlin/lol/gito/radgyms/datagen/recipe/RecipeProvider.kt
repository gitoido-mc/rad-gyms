package lol.gito.radgyms.datagen.recipe

import com.cobblemon.mod.common.api.pokeball.PokeBalls
import lol.gito.radgyms.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generate(recipeExporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.GYM_KEY, 1)
            .pattern("  g")
            .pattern(" b ")
            .pattern("   ")
            .input('g', Items.GOLD_INGOT)
            .input('b', PokeBalls.POKE_BALL.item())
            .group("multi_bench")
            .criterion(hasItem(Items.CRAFTING_TABLE), RecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
            .offerTo(recipeExporter)
    }
}