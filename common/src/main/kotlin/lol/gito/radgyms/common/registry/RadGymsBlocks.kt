/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.*
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour


object RadGymsBlocks : PlatformRegistry<Registry<Block>, ResourceKey<Registry<Block>>, Block>() {
    override val registry: Registry<Block> = BuiltInRegistries.BLOCK
    override val resourceKey: ResourceKey<Registry<Block>> = Registries.BLOCK

    @JvmField
    val GYM_ENTRANCE = this.create(
        modId("gym_entrance"),
        GymEntranceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS))
    )

    @JvmField
    val GYM_EXIT = this.create(
        modId("gym_exit"),
        GymExitBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS))
    )

    @JvmField
    val SHARD_BLOCK_COMMON = this.create(
        modId("shard_block_common"),
        CommonShardBlock()
    )

    @JvmField
    val SHARD_BLOCK_UNCOMMON = this.create(
        modId("shard_block_uncommon"),
        UncommonShardBlock()
    )

    @JvmField
    val SHARD_BLOCK_RARE = this.create(
        modId("shard_block_rare"),
        RareShardBlock()
    )

    @JvmField
    val SHARD_BLOCK_EPIC = this.create(
        modId("shard_block_epic"),
        EpicShardBlock()
    )
}