package lol.gito.radgyms.gui

import lol.gito.radgyms.RadGyms
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity

@Environment(EnvType.CLIENT)
object GuiHandler {
    @Environment(EnvType.CLIENT)
    fun openGymKeyScreen(player: PlayerEntity) {
        RadGyms.LOGGER.info("Open Gym Key Screen for player ${player.name}")
        MinecraftClient.getInstance().execute { MinecraftClient.getInstance().setScreen(GymKeyScreen(player)) }
    }
}