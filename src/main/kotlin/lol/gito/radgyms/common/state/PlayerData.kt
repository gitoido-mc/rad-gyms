/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.state

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation

class PlayerData(var visits: Int = 0, var returnCoords: ReturnCoords? = null) {
    data class ReturnCoords(val dimension: ResourceLocation, val position: BlockPos)

    fun incrementVisits() {
        visits++
    }
}