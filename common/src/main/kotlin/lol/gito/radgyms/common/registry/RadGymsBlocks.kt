/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.CommonShardBlock
import lol.gito.radgyms.common.block.EpicShardBlock
import lol.gito.radgyms.common.block.GymEntranceBlock
import lol.gito.radgyms.common.block.GymExitBlock
import lol.gito.radgyms.common.block.RareShardBlock
import lol.gito.radgyms.common.block.UncommonShardBlock
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

    private fun typeGemBlock(name: String): Block = this.create(
        cobblemonResource(name),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val GYM_ENTRANCE = this.create(
        modId("gym_entrance"),
        GymEntranceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS)),
    )

    @JvmField
    val GYM_EXIT = this.create(
        modId("gym_exit"),
        GymExitBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS)),
    )

    @JvmField
    val SHARD_BLOCK_COMMON = this.create(modId("shard_block_common"), CommonShardBlock())

    @JvmField
    val SHARD_BLOCK_UNCOMMON = this.create(modId("shard_block_uncommon"), UncommonShardBlock())

    @JvmField
    val SHARD_BLOCK_RARE = this.create(modId("shard_block_rare"), RareShardBlock())

    @JvmField
    val SHARD_BLOCK_EPIC = this.create(modId("shard_block_epic"), EpicShardBlock())

    @JvmField
    val TYPE_GEM_BLOCK_BUG = typeGemBlock("type_gem_block_bug")

    @JvmField
    val TYPE_GEM_BLOCK_DARK = typeGemBlock("type_gem_block_dark")

    @JvmField
    val TYPE_GEM_BLOCK_DRAGON = typeGemBlock("type_gem_block_dragon")

    @JvmField
    val TYPE_GEM_BLOCK_ELECTRIC = typeGemBlock("type_gem_block_electric")

    @JvmField
    val TYPE_GEM_BLOCK_FAIRY = typeGemBlock("type_gem_block_fairy")

    @JvmField
    val TYPE_GEM_BLOCK_FIGHTING = this.create(
        cobblemonResource("type_gem_block_fighting"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_FIRE = this.create(
        cobblemonResource("type_gem_block_fire"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_FLYING = this.create(
        cobblemonResource("type_gem_block_flying"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_GHOST = this.create(
        cobblemonResource("type_gem_block_ghost"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_GRASS = this.create(
        cobblemonResource("type_gem_block_grass"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_GROUND = this.create(
        cobblemonResource("type_gem_block_ground"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_ICE = this.create(
        cobblemonResource("type_gem_block_ice"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_NORMAL = this.create(
        cobblemonResource("type_gem_block_normal"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_POISON = this.create(
        cobblemonResource("type_gem_block_poison"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_PSYCHIC = this.create(
        cobblemonResource("type_gem_block_psychic"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_ROCK = this.create(
        cobblemonResource("type_gem_block_rock"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_STEEL = this.create(
        cobblemonResource("type_gem_block_steel"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )

    @JvmField
    val TYPE_GEM_BLOCK_WATER = this.create(
        cobblemonResource("type_gem_block_water"),
        Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK))
    )
}
