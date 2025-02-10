package lol.gito.radgyms.datagen.loot

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class GymLootDataProvider(
    output: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    lootContextType: LootContextType
) : SimpleFabricLootTableProvider(output, registryLookup, lootContextType) {
    override fun accept(lootTableBiConsumer: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>?) {

    }
}