/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.datagen

import lol.gito.radgyms.fabric.datagen.i18n.EnUSLocaleProvider
import lol.gito.radgyms.fabric.datagen.loot.BlockLootTableProvider
import lol.gito.radgyms.fabric.datagen.loot.GymLootDataProvider
import lol.gito.radgyms.fabric.datagen.loot.StructureLootDataProvider
import lol.gito.radgyms.fabric.datagen.recipe.RecipeProvider
import lol.gito.radgyms.fabric.datagen.tag.BlockTagDataProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack

@Suppress("unused")
object RadGymsDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: Pack = fabricDataGenerator.createPack()
        pack.addProvider(::EnUSLocaleProvider)
        pack.addProvider(::RecipeProvider)
        pack.addProvider(::BlockTagDataProvider)
        pack.addProvider(::GymLootDataProvider)
        pack.addProvider(::StructureLootDataProvider)
        pack.addProvider(::BlockLootTableProvider)
    }
}
