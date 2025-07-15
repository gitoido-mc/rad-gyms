/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms

import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.network.NetworkStackHandler.CacheOpen
import lol.gito.radgyms.network.NetworkStackHandler.GymLeave
import lol.gito.radgyms.network.NetworkStackHandler.handleCacheServerOpenPacket
import lol.gito.radgyms.network.NetworkStackHandler.handleGymServerLeavePacket
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.VillagerEntityRenderer

object RadGymsClient {
    @Environment(EnvType.CLIENT)
    fun init() {
        debug("Initializing client")
        EntityRendererRegistry.register(EntityManager.GYM_TRAINER) { context ->
            VillagerEntityRenderer(context)
        }

        // Networking
        CHANNEL.registerClientbound(GymLeave::class.java, ::handleGymServerLeavePacket)
        CHANNEL.registerClientbound(CacheOpen::class.java, ::handleCacheServerOpenPacket)
    }
}
