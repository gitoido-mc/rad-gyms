/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.state

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class PlayerData(var visits: Int = 0, var returnCoords: ReturnCoords? = null) {
    data class ReturnCoords(val dimension: Identifier, val position: BlockPos)

    fun incrementVisits() {
        visits++
    }
}