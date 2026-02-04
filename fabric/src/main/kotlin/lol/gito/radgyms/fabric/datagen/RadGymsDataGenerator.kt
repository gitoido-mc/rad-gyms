/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen

import lol.gito.radgyms.fabric.datagen.provider.*
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
        pack.addProvider(::CacheDataProvider)
    }
}
