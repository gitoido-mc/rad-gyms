/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.net.server.handler

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.gym.GymInitializer
import lol.gito.radgyms.common.gym.TeamGenerator
import lol.gito.radgyms.common.gym.TrainerFactory
import lol.gito.radgyms.common.gym.TrainerSpawner
import lol.gito.radgyms.common.net.client.payload.GymEnterC2S
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.util.TranslationUtil
import lol.gito.radgyms.common.util.displayClientMessage
import lol.gito.radgyms.common.world.StructurePlacer
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object GymEnterC2SHandler : ServerNetworkPacketHandler<GymEnterC2S> {
    override fun handle(
        packet: GymEnterC2S,
        server: MinecraftServer,
        player: ServerPlayer
    ) {
        RadGyms.debug("Using key? : ${packet.key}")
        var message = translatable(
            RadGyms.modId("message.info.gym_init").toLanguageKey(),
            TranslationUtil.buildTypeText(packet.type)
        )

        val type: String = when (packet.type) {
            "chaos", null -> ElementalTypes.all().random().showdownId
            else -> packet.type
        }

        if (packet.key) {
            val stack = player.mainHandItem

            if (stack.item == RadGymsItems.GYM_KEY) {
                val stackType =
                    stack.components
                        .getOrDefault(
                            RadGymsDataComponents.RG_GYM_TYPE_COMPONENT,
                            "chaos"
                        )
                        .let { it ?: packet.type }
                RadGyms.debug("Gym key type : $stackType")

                message = translatable(
                    RadGyms.modId("message.info.gym_init").toLanguageKey(),
                    TranslationUtil.buildTypeText(stackType)
                )

                stack.consume(1, player)
            } else {
                player.displayClientMessage(
                    translatable(
                        RadGyms.modId("message.error.key.not-in-main-hand").toLanguageKey()
                    )
                )
            }
        }

        if (packet.pos != null) {
            val gymEntrance: GymEntranceEntity = player.serverLevel().getBlockEntity(packet.pos) as GymEntranceEntity
            gymEntrance.incrementPlayerUseCount(player)
        }

        player.displayClientMessage(message)
        GymInitializer(
            templateRegistry = RadGyms.gymTemplateRegistry,
            trainerSpawner = TrainerSpawner(),
            structureManager = StructurePlacer,
            trainerFactory = TrainerFactory(),
            teamGenerator = TeamGenerator()
        ).initInstance(player, player.serverLevel(), packet.level, type)
    }
}