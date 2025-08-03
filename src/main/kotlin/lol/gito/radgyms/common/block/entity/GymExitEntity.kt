/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.block.entity

import lol.gito.radgyms.common.registry.BlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class GymExitEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(
        BlockEntityRegistry.GYM_EXIT_ENTITY,
        pos,
        state
    )
