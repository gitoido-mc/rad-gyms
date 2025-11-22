/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.datagen.loot

import lol.gito.radgyms.common.registry.BlockRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture


class BlockLootDataProvider(
    output: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : FabricBlockLootTableProvider(output, registryLookup) {
    override fun generate() {
        addDrop(BlockRegistry.SHARD_BLOCK_COMMON)
        addDrop(BlockRegistry.SHARD_BLOCK_UNCOMMON)
        addDrop(BlockRegistry.SHARD_BLOCK_RARE)
        addDrop(BlockRegistry.SHARD_BLOCK_EPIC)
    }
}