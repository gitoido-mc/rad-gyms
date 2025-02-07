package lol.gito.radgyms.gui

import com.cobblemon.mod.common.api.types.ElementalType
import lol.gito.radgyms.RadGyms
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity

@Environment(EnvType.CLIENT)
object GuiHandler {
    @Environment(EnvType.CLIENT)
    fun openGymKeyScreen(player: PlayerEntity) {
        MinecraftClient.getInstance().execute { MinecraftClient.getInstance().setScreen(GymKeyScreen(player)) }
    }
}