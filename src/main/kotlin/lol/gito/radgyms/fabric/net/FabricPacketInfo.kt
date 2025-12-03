package lol.gito.radgyms.fabric.net

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.net.PacketRegisterInfo
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer

class FabricPacketInfo<T : NetworkPacket<T>>(val info: PacketRegisterInfo<T>) {
    fun registerPacket(client: Boolean) {
        (if (client) {
            PayloadTypeRegistry.playS2C()
        } else {
            PayloadTypeRegistry.playC2S()
        })
            .register(info.payloadId, info.codec)
    }

    fun registerClientHandler() {
        ClientPlayNetworking.registerGlobalReceiver(info.payloadId) { obj, _ ->
            val handler = info.handler as ClientNetworkPacketHandler<T>
            handler.handle(obj, Minecraft.getInstance())
        }
    }

    fun registerServerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(info.payloadId) { obj, context ->
            val handler = info.handler as ServerNetworkPacketHandler<T>
            handler.handle(obj, context.player().server!!, context.player() as ServerPlayer)
        }
    }
}
