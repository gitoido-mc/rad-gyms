/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen

import lol.gito.radgyms.fabric.datagen.provider.BlockLootTableProvider
import lol.gito.radgyms.fabric.datagen.provider.BlockTagDataProvider
import lol.gito.radgyms.fabric.datagen.provider.CacheDataProvider
import lol.gito.radgyms.fabric.datagen.provider.EnUSLocaleProvider
import lol.gito.radgyms.fabric.datagen.provider.GymDataProvider
import lol.gito.radgyms.fabric.datagen.provider.GymLootDataProvider
import lol.gito.radgyms.fabric.datagen.provider.RecipeProvider
import lol.gito.radgyms.fabric.datagen.provider.StructureLootDataProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack

object RadGymsDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: Pack = fabricDataGenerator.createPack()
        pack.addProvider(::EnUSLocaleProvider)
        pack.addProvider(::RecipeProvider)
        pack.addProvider(::BlockTagDataProvider)
        pack.addProvider(::GymLootDataProvider)
        pack.addProvider(::StructureLootDataProvider)
        pack.addProvider(::BlockLootTableProvider)
        pack.addProvider(::GymDataProvider)
        pack.addProvider(::CacheDataProvider)
    }
}
