/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.datagen.tag

import lol.gito.radgyms.common.registry.RadGymsBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class BlockTagDataProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider<Block>(output, Registries.BLOCK, registriesFuture) {

    override fun addTags(wrapper: HolderLookup.Provider) {
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .setReplace(false)
            .add(RadGymsBlocks.GYM_ENTRANCE)
            .add(RadGymsBlocks.SHARD_BLOCK_COMMON)
            .add(RadGymsBlocks.SHARD_BLOCK_UNCOMMON)
            .add(RadGymsBlocks.SHARD_BLOCK_RARE)
            .add(RadGymsBlocks.SHARD_BLOCK_EPIC)
    }
}