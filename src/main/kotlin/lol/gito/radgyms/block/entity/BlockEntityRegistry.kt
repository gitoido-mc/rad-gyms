/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.block.entity

import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object BlockEntityRegistry {
    val GYM_ENTRANCE_ENTITY = registerBlockEntity(
        "gym_entrance_entity",
        ::GymEntranceEntity,
        BlockRegistry.GYM_ENTRANCE
    )

    val GYM_EXIT_ENTITY = registerBlockEntity(
        "gym_exit_entity",
        ::GymExitEntity,
        BlockRegistry.GYM_EXIT
    )

    private fun <T : BlockEntity> registerBlockEntity(
        name: String,
        entityFactory: BlockEntityFactory<out T>,
        vararg blocks: Block
    ): BlockEntityType<T> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        modId(name),
        BlockEntityType.Builder.create(entityFactory, *blocks).build()
    )

    fun register() {
        debug("Registering block entities")
    }
}
