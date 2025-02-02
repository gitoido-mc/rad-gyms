package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable

object GymKeyPacketHandler {
    operator fun invoke(player: ServerPlayerEntity, world: ServerWorld, level: Int) {
        val stack = player.mainHandStack

        if (stack.item == ItemRegistry.GYM_KEY) {
            player.sendMessage(Text.literal("packet received, level is ${level}. player: ${player.name}{"))
            if (GymManager.initInstance(player, world, level, ElementalTypes.all().random())) {
                stack.decrement(1)
            }
        } else {
            player.sendMessage(translatable(modIdentifier("message.error.key.not-in-main-hand").toTranslationKey()))
        }
    }
}