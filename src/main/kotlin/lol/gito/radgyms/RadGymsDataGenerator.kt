package lol.gito.radgyms

import lol.gito.radgyms.datagen.BlockTagDataProvider
import lol.gito.radgyms.datagen.EnUSLocaleProvider
import lol.gito.radgyms.datagen.PtBRLocaleProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack

@Suppress("unused")
object RadGymsDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: Pack = fabricDataGenerator.createPack()
        pack.addProvider(::EnUSLocaleProvider)
        pack.addProvider(::PtBRLocaleProvider)
        pack.addProvider(::BlockTagDataProvider)
    }
}