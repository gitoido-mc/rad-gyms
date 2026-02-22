/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.item.CommonPokeCache
import lol.gito.radgyms.common.item.CommonPokeShard
import lol.gito.radgyms.common.item.EpicPokeCache
import lol.gito.radgyms.common.item.EpicPokeShard
import lol.gito.radgyms.common.item.ExitRope
import lol.gito.radgyms.common.item.GymKey
import lol.gito.radgyms.common.item.GymRewardBag
import lol.gito.radgyms.common.item.RarePokeCache
import lol.gito.radgyms.common.item.RarePokeShard
import lol.gito.radgyms.common.item.UncommonPokeCache
import lol.gito.radgyms.common.item.UncommonPokeShard
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.Block

object RadGymsItems : PlatformRegistry<Registry<Item>, ResourceKey<Registry<Item>>, Item>() {
    override val registry: Registry<Item> = BuiltInRegistries.ITEM
    override val resourceKey: ResourceKey<Registry<Item>> = Registries.ITEM

    @JvmField
    val GYM_KEY: GymKey = this.create(modId("gym_key"), GymKey())

    @JvmField
    val GYM_REWARD: GymRewardBag = this.create(modId("gym_reward"), GymRewardBag())

    @JvmField
    val EXIT_ROPE: ExitRope = this.create(modId("exit_rope"), ExitRope())

    @JvmField
    val SHARD_COMMON: CommonPokeShard = this.create(modId("shard_common"), CommonPokeShard())

    @JvmField
    val SHARD_UNCOMMON: UncommonPokeShard = this.create(modId("shard_uncommon"), UncommonPokeShard())

    @JvmField
    val SHARD_RARE: RarePokeShard = this.create(modId("shard_rare"), RarePokeShard())

    @JvmField
    val SHARD_EPIC: EpicPokeShard = this.create(modId("shard_epic"), EpicPokeShard())

    @JvmField
    val CACHE_COMMON: CommonPokeCache = this.create(modId("cache_common"), CommonPokeCache())

    @JvmField
    val CACHE_UNCOMMON: UncommonPokeCache = this.create(modId("cache_uncommon"), UncommonPokeCache())

    @JvmField
    val CACHE_RARE: RarePokeCache = this.create(modId("cache_rare"), RarePokeCache())

    @JvmField
    val CACHE_EPIC: EpicPokeCache = this.create(modId("cache_epic"), EpicPokeCache())

    @JvmField
    val GYM_ENTRANCE = blockItem(modId("gym_entrance"), RadGymsBlocks.GYM_ENTRANCE)

    @JvmField
    val GYM_EXIT = blockItem(modId("gym_exit"), RadGymsBlocks.GYM_ENTRANCE)

    @JvmField
    val SHARD_BLOCK_COMMON = blockItem(modId("shard_block_common"), RadGymsBlocks.SHARD_BLOCK_COMMON)

    @JvmField
    val SHARD_BLOCK_UNCOMMON = blockItem(modId("shard_block_uncommon"), RadGymsBlocks.SHARD_BLOCK_UNCOMMON)

    @JvmField
    val SHARD_BLOCK_RARE = blockItem(modId("shard_block_rare"), RadGymsBlocks.SHARD_BLOCK_RARE)

    @JvmField
    val SHARD_BLOCK_EPIC = blockItem(modId("shard_block_epic"), RadGymsBlocks.SHARD_BLOCK_EPIC)

    private fun blockItem(name: ResourceLocation, block: Block, rarity: Rarity = Rarity.COMMON): BlockItem =
        this.create(name, BlockItem(block, Item.Properties().rarity(rarity)))
}
