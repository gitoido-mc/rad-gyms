package lol.gito.radgyms.network

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable

object GymLeavePacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
    ) {
        val stack = player.mainHandStack
        if (stack.item == ItemRegistry.EXIT_ROPE && !player.isCreative) {
            stack.decrement(1)
        }
        GymManager.handleGymLeave(player)
        player.sendMessage(translatable(modId("message.info.gym_failed").toTranslationKey()))
    }
}