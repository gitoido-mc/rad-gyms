package lol.gito.radgyms.network

import io.wispforest.owo.network.ServerAccess
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos


@Suppress("EmptyMethod")
object NetworkStackHandler {
    val GYM_KEY_PACKET_ID = modIdentifier("net.gym_key")
    val GYM_ENTRANCE_PACKET_ID = modIdentifier("net.gym_entrance")
    val GYM_LEAVE_PACKET_ID = modIdentifier("net.gym_leave")

    @JvmRecord
    data class GymKeyPacketMessage(
        val level: Int,
        val id: Identifier = GYM_KEY_PACKET_ID
    )

    @JvmRecord
    data class GymEntrancePacketMessage(
        val level: Int,
        val pos: BlockPos,
        val id: Identifier = GYM_ENTRANCE_PACKET_ID
    )

    @JvmRecord
    data class GymLeavePacketMessage(
        val id: Identifier = GYM_LEAVE_PACKET_ID
    )

    fun register() {
        RadGyms.LOGGER.info("Registering network stack handler")
        RadGyms.CHANNEL.registerServerbound(GymKeyPacketMessage::class.java, ::handleGymKeyPacket)
    }

    private fun handleGymKeyPacket(packet: GymKeyPacketMessage, context: ServerAccess) {
        val player = context.player()
        val world = player.world

        if (player is ServerPlayerEntity && world is ServerWorld) {
            world.server.execute { GymKeyPacketHandler(player, world, packet.level) }
        } else {
            player.sendMessage(translatable(modIdentifier("message.error.common.no-response").toTranslationKey()))
        }
    }

    private fun handleGymEntrancePacket(packet: GymEntrancePacketMessage, context: ServerAccess) {

    }

    private fun handleGymLeavePacket(packet: GymLeavePacketMessage, context: ServerAccess) {

    }
}