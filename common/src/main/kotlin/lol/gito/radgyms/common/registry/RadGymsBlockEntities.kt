/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.block.entity.GymExitEntity
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.entity.BlockEntityType

private typealias BETRegistry = Registry<BlockEntityType<*>>
private typealias BETRegistryKey = ResourceKey<Registry<BlockEntityType<*>>>

object RadGymsBlockEntities : PlatformRegistry<BETRegistry, BETRegistryKey, BlockEntityType<*>>() {
    override val registry: BETRegistry = BuiltInRegistries.BLOCK_ENTITY_TYPE
    override val resourceKey: BETRegistryKey = Registries.BLOCK_ENTITY_TYPE

    val GYM_ENTRANCE_ENTITY: BlockEntityType<GymEntranceEntity> = this.create(
        modId("gym_entrance_entity"),
        BlockEntityType.Builder.of(::GymEntranceEntity, RadGymsBlocks.GYM_ENTRANCE).build(null)
    )

    val GYM_EXIT_ENTITY: BlockEntityType<GymExitEntity> = this.create(
        modId("gym_exit_entity"),
        BlockEntityType.Builder.of(::GymExitEntity, RadGymsBlocks.GYM_EXIT).build(null)
    )
}
