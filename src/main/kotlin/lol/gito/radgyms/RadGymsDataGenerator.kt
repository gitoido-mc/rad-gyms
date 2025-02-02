package lol.gito.radgyms

import lol.gito.radgyms.datagen.i18n.LanguageDataProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack

object RadGymsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack: Pack = fabricDataGenerator.createPack();
		pack.addProvider(::LanguageDataProvider)
	}
}