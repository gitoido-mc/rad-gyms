package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.network.ClientAccess
import io.wispforest.owo.network.ServerAccess
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos

@Suppress("EmptyMethod")
object NetworkStackHandler {
    val PACKET_ENTER_BLOCK = modId("net.gym_enter_block")
    val PACKET_ENTER_KEY = modId("net.gym_enter_key")
    val PACKET_LEAVE = modId("net.gym_leave")
    val PACKET_OPEN_CACHE = modId("net.cache_open")

    @JvmRecord
    data class GymEnterWithCoords(
        val id: Identifier = PACKET_ENTER_BLOCK,
        val level: Int,
        val type: String,
        val blockPos: Long
    )

    @JvmRecord
    data class GymEnterWithoutCoords(
        val id: Identifier = PACKET_ENTER_KEY,
        val level: Int,
        val type: String
    )

    @JvmRecord
    data class GymLeave(
        val id: Identifier = PACKET_LEAVE,
        val teleport: Boolean
    )

    @JvmRecord
    data class CacheOpen(
        val id: Identifier = PACKET_OPEN_CACHE,
        val type: String,
        val rarity: Rarity,
        val shinyBoost: Int,
    )

    fun register() {
        debug("Registering network stack handler")
        CHANNEL.registerServerbound(GymEnterWithCoords::class.java, ::handleGymEnterBlockPacket)
        CHANNEL.registerServerbound(GymEnterWithoutCoords::class.java, ::handleGymEnterKeyPacket)
        CHANNEL.registerServerbound(GymLeave::class.java, ::handleGymLeavePacket)
        CHANNEL.registerServerbound(CacheOpen::class.java, ::handleCacheOpenPacket)
        CHANNEL.registerClientboundDeferred(CacheOpen::class.java)
        CHANNEL.registerClientboundDeferred(GymLeave::class.java)
    }

    private fun handleGymEnterBlockPacket(packet: GymEnterWithCoords, context: ServerAccess) {
        val serverPlayer = context.player() as ServerPlayerEntity
        val serverWorld = serverPlayer.world as ServerWorld

        val blockPos = BlockPos.fromLong(packet.blockPos)
        if (serverWorld.getBlockEntity(blockPos) is GymEntranceEntity) {
            val gymEntrance = serverPlayer.world.getBlockEntity(blockPos) as GymEntranceEntity
            gymEntrance.incrementPlayerUseCount(serverPlayer)
            debug("Gym Entrance at $blockPos - increased usage for player ${serverPlayer.name.string}")
        }
        serverWorld.chunkManager.markForUpdate(blockPos)
        executeEnter(serverPlayer, serverWorld, packet.level, packet.type, false)
    }

    private fun handleGymEnterKeyPacket(packet: GymEnterWithoutCoords, context: ServerAccess) {
        val serverPlayer = context.player() as ServerPlayerEntity
        val serverWorld = serverPlayer.world as ServerWorld

        executeEnter(serverPlayer, serverWorld, packet.level, packet.type, true)
    }

    private fun executeEnter(
        serverPlayer: ServerPlayerEntity,
        serverWorld: ServerWorld,
        level: Int,
        type: String,
        key: Boolean
    ) {
        debug("Handling GymEnter packet for player ${serverPlayer.name}")
        serverWorld.server.execute {
            GymEnterPacketHandler(
                serverPlayer,
                serverWorld,
                level,
                key,
                type
            )
        }
    }

    private fun handleGymLeavePacket(packet: GymLeave, context: ServerAccess) {
        val serverPlayer = context.player()
        debug("Handling GymLeave ${packet.id} packet for player ${serverPlayer.name}")

        context.player.server.execute { GymLeavePacketHandler(context.player) }
    }

    private fun handleCacheOpenPacket(packet: CacheOpen, context: ServerAccess) {
        debug("Handling CacheOpen packet for player ${context.player.name}")
        context.player.server.execute {
            CacheOpenPacketHandler(
                context.player,
                ElementalTypes.get(packet.type)!!,
                packet.rarity,
                packet.shinyBoost,
            )
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun handleGymServerLeavePacket(packet: GymLeave, context: ClientAccess) {
        CHANNEL.clientHandle().send(packet)
    }

    @Suppress("UNUSED_PARAMETER")
    fun handleCacheServerOpenPacket(packet: CacheOpen, context: ClientAccess) {
        CHANNEL.clientHandle().send(packet)
    }
}
