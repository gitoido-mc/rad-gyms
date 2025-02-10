package lol.gito.radgyms.network

import io.wispforest.owo.network.ServerAccess
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos


@Suppress("EmptyMethod")
object NetworkStackHandler {
    val GYM_KEY_PACKET_ID = modIdentifier("net.gym_enter")
    val GYM_LEAVE_PACKET_ID = modIdentifier("net.gym_leave")

    @JvmRecord
    data class GymEnter(
        val id: Identifier = GYM_KEY_PACKET_ID,
        val level: Int,
        val type: String,
        val blockPos: Long? = null
    )

    @JvmRecord
    data class GymLeave(
        val id: Identifier = GYM_LEAVE_PACKET_ID
    )

    fun register() {
        RadGyms.LOGGER.info("Registering network stack handler")
        RadGyms.CHANNEL.registerServerbound(GymEnter::class.java, ::handleGymEnterPacket)
        RadGyms.CHANNEL.registerServerbound(GymLeave::class.java, ::handleGymLeavePacket)
    }

    private fun handleGymEnterPacket(packet: GymEnter, context: ServerAccess) {
        val player = context.player()
        val world = player.world

        if (player is ServerPlayerEntity && world is ServerWorld) {
            if (packet.blockPos != null) {
                val blockPos = BlockPos.fromLong(packet.blockPos)
                if (world.getBlockEntity(blockPos) is GymEntranceEntity) {
                    val gymEntrance = player.world.getBlockEntity(blockPos) as GymEntranceEntity
                    gymEntrance.incrementPlayerUseCount(player)
                    RadGyms.LOGGER.info("Gym Entrance at ${blockPos.asLong()} - increased usage for player ${player.name.literalString}")
                }
                world.chunkManager.markForUpdate(blockPos)
            }
            world.server.execute { GymEnterPacketHandler(player, world, packet.level, packet.blockPos == null, packet.type) }
        } else {
            player.sendMessage(translatable(modIdentifier("message.error.common.no-response").toTranslationKey()))
        }
    }

    private fun handleGymLeavePacket(packet: GymLeave, context: ServerAccess) {
        val player = context.player()
        val world = player.world

        if (player is ServerPlayerEntity && world is ServerWorld) {
            world.server.execute { GymLeavePacketHandler(player) }
        } else {
            player.sendMessage(translatable(modIdentifier("message.error.common.no-response").toTranslationKey()))

        }
    }
}