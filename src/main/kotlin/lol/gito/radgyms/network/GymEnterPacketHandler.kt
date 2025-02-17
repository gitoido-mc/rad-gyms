package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemRegistry
import lol.gito.radgyms.item.dataComponent.DataComponentManager.GYM_TYPE_COMPONENT
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable

object GymEnterPacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
        world: ServerWorld,
        level: Int,
        key: Boolean = false,
        type: String
    ) {
        var message = translatable(
            modId("message.info.gym_init").toTranslationKey(),
            type
        )

        if (key) {
            val stack = player.mainHandStack

            if (stack.item == ItemRegistry.GYM_KEY) {
                val stackType = stack.components.get(GYM_TYPE_COMPONENT).let { it ?: type }

                if (stackType !in ElementalTypes.all().map { it.name }) {
                    message = translatable(
                        modId("message.info.gym_init").toTranslationKey(),
                        translatable(modId("custom_type.$stackType").toTranslationKey())
                    )
                }

                player.sendMessage(message)
                if (GymManager.initInstance(player, world, level, stackType)) {
                    if (!player.isCreative) stack.decrement(1)
                }
            } else {
                player.sendMessage(translatable(modId("message.error.key.not-in-main-hand").toTranslationKey()))
            }
        } else {
            player.sendMessage(message)
            GymManager.initInstance(player, world, level, type)
        }

    }
}