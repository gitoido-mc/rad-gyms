package lol.gito.radgyms.rct

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
        val player = handler.player
        RadGyms.LOGGER.info("registering player from RCT trainer mod registry")
        RCT.trainerRegistry.registerPlayer(player.uuid.toString(), player)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onPlayerDisconnect(handler: ServerPlayNetworkHandler, server: MinecraftServer) {
        RadGyms.LOGGER.info("unregistering player from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(handler.player.uuid.toString())
    }

    fun register() {
        ServerLifecycleEvents.SERVER_STARTING.register(RCTEventManager::onServerStart)
        ServerPlayConnectionEvents.JOIN.register(RCTEventManager::onPlayerJoin)
        ServerPlayConnectionEvents.DISCONNECT.register(RCTEventManager::onPlayerDisconnect)
    }
}