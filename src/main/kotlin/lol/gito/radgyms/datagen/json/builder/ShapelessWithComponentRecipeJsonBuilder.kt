package lol.gito.radgyms.datagen.json.builder

import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.component.ComponentMap
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList

class ShapelessWithComponentRecipeJsonBuilder(
    private val category: RecipeCategory,
    private val output: Item,
    private val count: Int = 1
) : CraftingRecipeJsonBuilder {
    private val inputs: DefaultedList<Ingredient> = DefaultedList.of()
    private val advancementBuilder: MutableMap<String?, AdvancementCriterion<*>?> = mutableMapOf()
    private var itemStack: ItemStack = ItemStack(this.output, this.count)
    private var group: String? = null

    fun input(tag: TagKey<Item>): ShapelessWithComponentRecipeJsonBuilder = this.input(Ingredient.fromTag(tag))
    fun input(itemProvider: ItemConvertible): ShapelessWithComponentRecipeJsonBuilder = this.input(itemProvider, 1)
    fun input(ingredient: Ingredient): ShapelessWithComponentRecipeJsonBuilder = this.input(ingredient, 1)

    fun input(itemProvider: ItemConvertible?, size: Int): ShapelessWithComponentRecipeJsonBuilder {
        for (i in 0..<size) {
            this.input(Ingredient.ofItems(itemProvider))
        }
        return this
    }

    fun input(ingredient: Ingredient, size: Int): ShapelessWithComponentRecipeJsonBuilder {
        for (i in 0..<size) {
            inputs.add(ingredient)
        }
        return this
    }

    fun withComponentMap(map: ComponentMap): ShapelessWithComponentRecipeJsonBuilder {
        return this.apply {
            this.itemStack.applyComponentsFrom(map)
        }
    }

    override fun criterion(
        string: String?,
        advancementCriterion: AdvancementCriterion<*>?
    ): ShapelessWithComponentRecipeJsonBuilder {
        advancementBuilder[string] = advancementCriterion
        return this
    }

    override fun group(string: String?): ShapelessWithComponentRecipeJsonBuilder {
        this.group = string
        return this
    }

    override fun getOutputItem(): Item {
        return this.output
    }

    override fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        this.validate(recipeId)
        val builder = exporter.advancementBuilder
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
        advancementBuilder.forEach { (name: String?, criterion: AdvancementCriterion<*>?) ->
            builder.criterion(
                name,
                criterion
            )
        }
        val shapelessRecipe = ShapelessRecipe(
            this.group ?: "",
            CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
            this.itemStack,
            this.inputs
        )
        exporter.accept(
            recipeId,
            shapelessRecipe,
            builder.build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/"))
        )
    }

    private fun validate(recipeId: Identifier) {
        check(advancementBuilder.isNotEmpty()) { "No way of obtaining recipe $recipeId" }
    }
}