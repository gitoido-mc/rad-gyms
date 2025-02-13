package lol.gito.radgyms.network

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gym.GymManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable

object GymLeavePacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
    ) {
        player.sendMessage(translatable(modId("message.info.gym_failed").toTranslationKey()))
        GymManager.handleGymLeave(player)
    }
}