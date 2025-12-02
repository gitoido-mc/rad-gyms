/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.datagen.loot

import lol.gito.radgyms.common.registry.RadGymsBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item

class BlockLootDataProvider(lookup: HolderLookup.Provider) :
    BlockLootSubProvider(mutableSetOf<Item>(), FeatureFlags.DEFAULT_FLAGS, lookup) {
    override fun generate() {
        dropSelf(RadGymsBlocks.SHARD_BLOCK_COMMON)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_UNCOMMON)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_RARE)
        dropSelf(RadGymsBlocks.SHARD_BLOCK_EPIC)
    }
}