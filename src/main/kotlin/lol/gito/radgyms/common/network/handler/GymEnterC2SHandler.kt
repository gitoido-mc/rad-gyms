/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.network.payload.GymEnterC2S
import lol.gito.radgyms.common.registry.DataComponentRegistry
import lol.gito.radgyms.common.registry.ItemRegistry
import lol.gito.radgyms.common.util.TranslationUtil
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text

class GymEnterC2SHandler(payload: GymEnterC2S, context: ServerPlayNetworking.Context) {
    init {
        RadGyms.debug("Using key? : ${payload.key}")
        var message = Text.translatable(
            RadGyms.modId("message.info.gym_init").toTranslationKey(),
            TranslationUtil.buildTypeText(payload.type)
        )

        if (payload.key) {
            val stack = context.player().mainHandStack

            if (stack.item == ItemRegistry.GYM_KEY) {
                val stackType =
                    stack.components.get(DataComponentRegistry.GYM_TYPE_COMPONENT).let { it ?: payload.type }
                RadGyms.debug("Gym key type : $stackType")

                message = Text.translatable(
                    RadGyms.modId("message.info.gym_init").toTranslationKey(),
                    TranslationUtil.buildTypeText(stackType)
                )

                if (!context.player().isCreative) stack.decrement(1)
            } else {
                context.player().sendMessage(
                    Text.translatable(
                        RadGyms.modId("message.error.key.not-in-main-hand").toTranslationKey()
                    )
                )
            }
        }

        if (payload.pos != null) {
            val gymEntrance: GymEntranceEntity = context.player().world.getBlockEntity(payload.pos) as GymEntranceEntity
            gymEntrance.incrementPlayerUseCount(context.player())
        }

        context.player().sendMessage(message)
        GymManager.initInstance(context.player(), context.player().serverWorld, payload.level, payload.type)
    }
}