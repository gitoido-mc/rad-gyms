/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block.entity

import lol.gito.radgyms.common.registry.RadGymsBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GymExitEntity(pos: BlockPos, state: BlockState) : BlockEntity(
    RadGymsBlockEntities.GYM_EXIT_ENTITY,
    pos,
    state
)
