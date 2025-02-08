package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable

object GymLeavePacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
    ) {
        player.sendMessage(Text.literal("packet received, player: ${player.name.literalString}"))

    }
}