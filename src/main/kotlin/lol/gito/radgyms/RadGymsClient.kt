package lol.gito.radgyms

import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.block.entity.BlockEntityRegistry
import lol.gito.radgyms.client.renderer.GymEntranceEntityRenderer
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.network.NetworkStackHandler.CacheOpen
import lol.gito.radgyms.network.NetworkStackHandler.GymLeave
import lol.gito.radgyms.network.NetworkStackHandler.handleCacheServerOpenPacket
import lol.gito.radgyms.network.NetworkStackHandler.handleGymServerLeavePacket
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.VillagerEntityRenderer

object RadGymsClient {
    @Environment(EnvType.CLIENT)
    fun init() {
        debug("Initializing client")
        EntityRendererRegistry.register(EntityManager.GYM_TRAINER) { context ->
            VillagerEntityRenderer(context)
        }
        BlockEntityRendererFactories.register(BlockEntityRegistry.GYM_ENTRANCE_ENTITY) { context ->
            GymEntranceEntityRenderer(context)
        }

        // Networking
        CHANNEL.registerClientbound(GymLeave::class.java, ::handleGymServerLeavePacket)
        CHANNEL.registerClientbound(CacheOpen::class.java, ::handleCacheServerOpenPacket)
    }
}
