package lol.gito.radgyms.rct

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.gitlab.srcmc.rctapi.api.events.Events
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.RCT
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler

object RCTEventManager {
    private fun onServerStart(server: MinecraftServer) {
        val trainerRegistry = RCT.trainerRegistry;
        RadGyms.LOGGER.info("initializing RCT trainer mod registry")
        trainerRegistry.init(server)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onPlayerJoin(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
        RadGyms.LOGGER.info("Adding player ${handler.player.name} in RadGyms trainer registry")
        RCT.trainerRegistry.registerPlayer(handler.player.uuid.toString(), handler.player)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onPlayerDisconnect(handler: ServerPlayNetworkHandler, server: MinecraftServer) {
        RadGyms.LOGGER.info("Removing player ${handler.player.name} from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(handler.player.uuid.toString())
    }


    fun register() {
        ServerLifecycleEvents.SERVER_STARTING.register(::onServerStart)
        ServerPlayConnectionEvents.JOIN.register(::onPlayerJoin)
        ServerPlayConnectionEvents.DISCONNECT.register(::onPlayerDisconnect)
    }
}