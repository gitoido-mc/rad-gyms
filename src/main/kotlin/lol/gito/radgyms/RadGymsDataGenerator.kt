package lol.gito.radgyms

import lol.gito.radgyms.datagen.recipe.RecipeProvider
import lol.gito.radgyms.datagen.tag.BlockTagDataProvider
import lol.gito.radgyms.datagen.i18n.EnUSLocaleProvider
import lol.gito.radgyms.datagen.i18n.PtBRLocaleProvider
import lol.gito.radgyms.datagen.loot.GymLootDataProvider
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
        pack.addProvider(::RecipeProvider)
        pack.addProvider(::GymLootDataProvider)
    }
}