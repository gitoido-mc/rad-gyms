package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable

object GymEnterPacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
        world: ServerWorld,
        level: Int,
        key: Boolean = false,
        type: String = ElementalTypes.all().random().name
    ) {
        player.sendMessage(Text.literal("Initializing level $level $type gym"))
        if (key) {
            val stack = player.mainHandStack

            if (stack.item == ItemRegistry.GYM_KEY) {

                RadGyms.LOGGER.info("Chosen type: $type")
                if (GymManager.initInstance(player, world, level, type)) {
                    stack.decrement(1)
                }
            } else {
                player.sendMessage(translatable(modIdentifier("message.error.key.not-in-main-hand").toTranslationKey()))
            }
        } else {
            GymManager.initInstance(player, world, level, type)
        }
    }
}