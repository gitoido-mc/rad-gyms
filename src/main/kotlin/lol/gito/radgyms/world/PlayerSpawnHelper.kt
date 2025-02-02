package lol.gito.radgyms.world

import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object PlayerSpawnHelper {
    fun getUniquePlayerCoords(player: ServerPlayerEntity, world: ServerWorld): BlockPos {
        val playerX: Long = player.uuid.mostSignificantBits and 30000000L // get uniq x coord based on player uuid
        val playerZ: Int = GymsNbtData.incrementVisitCount(player as EntityDataSaver) * 128

        val chunk = world.getChunk(BlockPos(
            playerX.toInt(),
            0,
            playerZ,
        ))

        return chunk.pos.startPos
    }
}