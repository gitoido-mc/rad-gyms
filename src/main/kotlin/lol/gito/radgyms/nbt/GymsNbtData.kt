package lol.gito.radgyms.nbt

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

object GymsNbtData {
    fun incrementVisitCount(player: EntityDataSaver): Int {
        val nbt: NbtCompound = player.getPersistentData()
        var count: Int = nbt.getInt("gyms_visited_count")

        count++

        nbt.putInt("gyms_visited_count", count)

        return count
    }

    fun getReturnDimension(player: EntityDataSaver): String? {
        val nbt: NbtCompound = player.getPersistentData()

        if (nbt.contains("gyms_return_dim")) {
            return nbt.getString("gyms_return_dim")
        }
        return null
    }

    fun setReturnDimension(player: EntityDataSaver, dim: String?): String? {
        val nbt: NbtCompound = player.getPersistentData()
        var nbtDim: String = nbt.getString("gyms_return_dim")

        if (dim != null) {
            nbtDim = dim
            nbt.putString("gyms_return_dim", nbtDim)
            return nbtDim
        } else {
            nbt.remove("gyms_return_dim")
        }

        return null
    }

    fun getReturnCoordinates(player: EntityDataSaver): BlockPos? {
        val nbt: NbtCompound = player.getPersistentData()

        if (nbt.contains("gyms_return_coords")) {
            val data = nbt.getIntArray("gyms_return_coords")

            return BlockPos(
                data[0],
                data[1],
                data[2],
            )
        }

        return null
    }

    fun setReturnCoordinates(player: EntityDataSaver, coords: BlockPos?): BlockPos? {
        val nbt: NbtCompound = player.getPersistentData()
        var nbtPos: IntArray = nbt.getIntArray("gyms_return_coords")

        if (coords != null) {
            nbtPos = intArrayOf(coords.x, coords.y, coords.z)
            nbt.putIntArray("gyms_return_coords", nbtPos)
            return coords
        } else {
            nbt.remove("gyms_return_coords")
        }

        return null
    }
}