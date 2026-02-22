/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.server.handler

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.ElementalTypeTranslationHelper
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.net.client.payload.GymEnterC2S
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.registry.RadGymsStats.getStat
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object GymEnterC2SHandler : ServerNetworkPacketHandler<GymEnterC2S> {
    override fun handle(packet: GymEnterC2S, server: MinecraftServer, player: ServerPlayer) {
        RadGyms.debug("Using key? : ${packet.key}")
        var message = tl("message.info.gym_init", ElementalTypeTranslationHelper.buildTypeText(packet.type))

        val type: String =
            when (packet.type) {
                "chaos", null -> ElementalTypes.all().random().showdownId
                else -> packet.type
            }

        if (packet.key) {
            val stack = player.mainHandItem

            if (stack.item == RadGymsItems.GYM_KEY) {
                val stackType =
                    stack.components
                        .getOrDefault(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, "chaos")
                        .let { it ?: packet.type }
                RadGyms.debug("Gym key type : $stackType")

                message = tl("message.info.gym_init", ElementalTypeTranslationHelper.buildTypeText(stackType))

                stack.consume(1, player)
                player.awardStat(getStat(RadGyms.statistics.KEYS_USED))
            } else {
                player.displayClientMessage(tl("message.error.key.not-in-main-hand"))
            }
        }

        if (packet.pos != null) {
            val gymEntrance: GymEntranceEntity = player.serverLevel().getBlockEntity(packet.pos) as GymEntranceEntity
            gymEntrance.incrementPlayerUseCount(player)
            player.awardStat(getStat(RadGyms.statistics.ENTRANCES_USED))
        }

        player.displayClientMessage(message)
        RadGyms.gymInitializer.initInstance(player, player.serverLevel(), packet.level, type, packet.key)
    }
}
