package lol.gito.radgyms.network

import io.wispforest.owo.network.ServerAccess
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier


@Suppress("EmptyMethod")
object NetworkStackHandler {
    val GYM_KEY_PACKET_ID = modIdentifier("net.gym_enter")
    val GYM_LEAVE_PACKET_ID = modIdentifier("net.gym_leave")

    @JvmRecord
    data class GymEnter(
        val id: Identifier = GYM_KEY_PACKET_ID,
        val level: Int,
        val type: String,
        val key: Boolean = false
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
            world.server.execute { GymEnterPacketHandler(player, world, packet.level, packet.key, packet.type) }
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