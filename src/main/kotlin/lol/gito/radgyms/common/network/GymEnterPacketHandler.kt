/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.cobblemonResource
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.registry.DataComponentRegistry.GYM_TYPE_COMPONENT
import lol.gito.radgyms.common.registry.ItemRegistry
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
        debug("Using key? : $key")
        var message = translatable(
            modId("message.info.gym_init").toTranslationKey(),
            type
        )

        when {
            key -> {
                val stack = player.mainHandStack

                when (stack.item) {
                    ItemRegistry.GYM_KEY -> {
                        val stackType = stack.components.get(GYM_TYPE_COMPONENT).let { it ?: type }
                        debug("Gym key type : $stackType")

                        message = translatable(
                            modId("message.info.gym_init").toTranslationKey(),
                            translatable(
                                when (stackType) {
                                    !in ElementalTypes.all().map { it.name } -> {
                                        modId("custom_type.$stackType").toTranslationKey()
                                    }

                                    else -> {
                                        cobblemonResource("type.$stackType").toTranslationKey()
                                    }
                                }
                            )
                        )

                        player.sendMessage(message)
                        GymManager.initInstance(player, world, level, stackType)
                        if (!player.isCreative) stack.decrement(1)
                    }

                    else -> {
                        player.sendMessage(translatable(modId("message.error.key.not-in-main-hand").toTranslationKey()))
                    }
                }
            }

            else -> {
                player.sendMessage(message)
                GymManager.initInstance(player, world, level, type)
            }
        }
    }
}
