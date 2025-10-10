/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.api.annotation.RegisterBlockItem
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.*
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties


object BlockRegistry {
    val GYM_EXIT: GymExitBlock = Registry.register(
        Registries.BLOCK,
        modId("gym_exit"),
        GymExitBlock(AbstractBlock.Settings.copy(Blocks.END_STONE_BRICKS))
    )

    @RegisterBlockItem("gym_entrance")
    val GYM_ENTRANCE: GymEntranceBlock = Registry.register(
        Registries.BLOCK,
        modId("gym_entrance"),
        GymEntranceBlock(AbstractBlock.Settings.copy(Blocks.END_STONE_BRICKS))
    )

    @RegisterBlockItem("shard_block_common")
    val SHARD_BLOCK_COMMON: CommonShardBlock = Registry.register(
        Registries.BLOCK,
        modId("shard_block_common"),
        CommonShardBlock()
    )

    @RegisterBlockItem("shard_block_uncommon")
    val SHARD_BLOCK_UNCOMMON: UncommonShardBlock = Registry.register(
        Registries.BLOCK,
        modId("shard_block_uncommon"),
        UncommonShardBlock()
    )

    @RegisterBlockItem("shard_block_rare")
    val SHARD_BLOCK_RARE: RareShardBlock = Registry.register(
        Registries.BLOCK,
        modId("shard_block_rare"),
        RareShardBlock()
    )

    @RegisterBlockItem("shard_block_epic")
    val SHARD_BLOCK_EPIC: EpicShardBlock = Registry.register(
        Registries.BLOCK,
        modId("shard_block_epic"),
        EpicShardBlock()
    )

    fun register() {
        debug("Registering blocks")

        this::class.memberProperties.forEach { property ->
            if (!property.hasAnnotation<RegisterBlockItem>()) return@forEach
            if (property.visibility != KVisibility.PUBLIC) return@forEach

            val block = property.getter.call(this)
            if (block !is Block) return@forEach
            debug("Registering ${block.name.string}")

            val annotation = property.findAnnotation<RegisterBlockItem>()
            val settings = when (block) {
                is PokeShardBlockBase -> Item.Settings().rarity(block.rarity)
                else -> Item.Settings()
            }

            Registry.register(
                Registries.ITEM,
                modId(annotation!!.identifier),
                BlockItem(block, settings)
            )
        }
    }
}