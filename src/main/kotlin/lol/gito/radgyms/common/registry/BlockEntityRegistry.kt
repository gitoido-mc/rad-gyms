/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.block.entity.GymExitEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object BlockEntityRegistry {
    val GYM_ENTRANCE_ENTITY: BlockEntityType<GymEntranceEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        modId("gym_entrance_entity"),
        BlockEntityType.Builder.create(::GymEntranceEntity, BlockRegistry.GYM_ENTRANCE).build()
    )

    val GYM_EXIT_ENTITY: BlockEntityType<GymExitEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        modId("gym_exit_entity"),
        BlockEntityType.Builder.create(::GymExitEntity, BlockRegistry.GYM_EXIT).build()
    )

    fun register() {
        debug("Registering block entities")
    }
}