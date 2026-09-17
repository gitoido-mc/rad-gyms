/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.world.state.dto

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation

data class PlayerData(var visits: Int = 0, var returnCoords: ReturnCoords? = null) {
    data class ReturnCoords(val dimension: ResourceLocation, val position: BlockPos)

    fun incrementVisits() {
        visits++
    }
}
