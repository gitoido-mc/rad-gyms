/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import lol.gito.radgyms.common.registry.RadGymsBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class BlockLootTableProvider(
    output: FabricDataOutput,
    lookup: CompletableFuture<HolderLookup.Provider>,
) : FabricBlockLootTableProvider(output, lookup) {
    override fun generate() {
        dropSelf(RadGymsBlocks.SHARD_BLOCK_COMMON)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_UNCOMMON)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_RARE)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_EPIC)
    }
}
