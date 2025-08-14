/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.nbt

import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

object GymsNbtData {
    private val visitedCountKey: String = modId("visited_count").toTranslationKey()
    private val returnDimensionKey: String = modId("return_dim").toTranslationKey()
    private val returnCoordsKey: String = modId("return_coords").toTranslationKey()

    fun incrementVisitCount(player: EntityDataSaver): Int {
        val nbt: NbtCompound = player.`RadGyms$getGymsPersistentData`()
        var count: Int = nbt.getInt(visitedCountKey)

        count++

        nbt.putInt(visitedCountKey, count)

        return count
    }

    fun getReturnDimension(player: EntityDataSaver): String? {
        val nbt: NbtCompound = player.`RadGyms$getGymsPersistentData`()
        if (nbt.contains(returnDimensionKey)) {
            return nbt.getString(returnDimensionKey)
        }
        return null
    }

    fun setReturnDimension(player: EntityDataSaver, dim: String?): String? {
        val nbt: NbtCompound = player.`RadGyms$getGymsPersistentData`()
        if (dim != null) {
            nbt.putString(returnDimensionKey, dim)
            return dim
        } else {
            nbt.remove(returnDimensionKey)
        }

        return null
    }

    fun getReturnCoordinates(player: EntityDataSaver): BlockPos? {
        val nbt: NbtCompound = player.`RadGyms$getGymsPersistentData`()

        if (nbt.contains(returnCoordsKey)) {
            val data = nbt.getIntArray(returnCoordsKey)

            return BlockPos(
                data[0],
                data[1],
                data[2],
            )
        }

        return null
    }

    fun setReturnCoordinates(player: EntityDataSaver, coords: BlockPos?): BlockPos? {
        val nbt: NbtCompound = player.`RadGyms$getGymsPersistentData`()

        if (coords != null) {
            val nbtPos = intArrayOf(coords.x, coords.y, coords.z)
            nbt.putIntArray(returnCoordsKey, nbtPos)
            return coords
        } else {
            nbt.remove(returnCoordsKey)
        }

        return null
    }
}
