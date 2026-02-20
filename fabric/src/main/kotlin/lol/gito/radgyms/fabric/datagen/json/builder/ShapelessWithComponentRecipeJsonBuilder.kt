/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.json.builder

import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentMap
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class ShapelessWithComponentRecipeJsonBuilder(
    private val category: RecipeCategory,
    private val output: Item,
    private val count: Int = 1,
) : RecipeBuilder {
    private val inputs: NonNullList<Ingredient> = NonNullList.create()
    private val advancementBuilder: MutableMap<String?, Criterion<*>?> = mutableMapOf()
    private var itemStack: ItemStack = ItemStack(this.output, this.count)
    private var group: String? = null

    private fun validate(recipeId: ResourceLocation) = check(advancementBuilder.isNotEmpty()) {
        "No way of obtaining recipe $recipeId"
    }

    override fun getResult(): Item = this.output

    override fun group(string: String?): ShapelessWithComponentRecipeJsonBuilder =
        this.apply {
            group = string
        }

    @Suppress("unused")
    fun define(tag: TagKey<Item>): ShapelessWithComponentRecipeJsonBuilder = this.define(Ingredient.of(tag))

    fun define(itemProvider: ItemLike): ShapelessWithComponentRecipeJsonBuilder = this.define(itemProvider, 1)

    fun define(ingredient: Ingredient): ShapelessWithComponentRecipeJsonBuilder = this.define(ingredient, 1)

    fun define(
        itemProvider: ItemLike?,
        size: Int,
    ): ShapelessWithComponentRecipeJsonBuilder =
        this.apply {
            for (_ in 0..<size) {
                define(Ingredient.of(itemProvider))
            }
        }

    fun define(
        ingredient: Ingredient,
        size: Int,
    ): ShapelessWithComponentRecipeJsonBuilder =
        this.apply {
            for (_ in 0..<size) {
                inputs.add(ingredient)
            }
        }

    fun withComponentMap(map: DataComponentMap): ShapelessWithComponentRecipeJsonBuilder =
        this.apply {
            itemStack.applyComponents(map)
        }

    override fun unlockedBy(
        string: String,
        criterion: Criterion<*>,
    ): RecipeBuilder =
        this.apply {
            advancementBuilder[string] = criterion
        }

    override fun save(
        exporter: RecipeOutput,
        recipeId: ResourceLocation,
    ) {
        this.validate(recipeId)
        val builder =
            exporter
                .advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR)
        advancementBuilder.forEach { (name: String?, criterion: Criterion<*>?) ->
            builder.addCriterion(
                name!!,
                criterion!!,
            )
        }
        val shapelessRecipe =
            ShapelessRecipe(
                this.group ?: "",
                RecipeBuilder.determineBookCategory(this.category),
                this.itemStack,
                this.inputs,
            )
        exporter.accept(
            recipeId,
            shapelessRecipe,
            builder.build(recipeId.withPrefix("recipes/" + category.name.lowercase() + "/")),
        )
    }
}
