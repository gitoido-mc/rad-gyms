package lol.gito.radgyms.datagen

import lol.gito.radgyms.block.BlockRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class BlockTagDataProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {

    override fun configure(wrapper: RegistryWrapper.WrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(BlockRegistry.GYM_ENTRANCE)
    }
}