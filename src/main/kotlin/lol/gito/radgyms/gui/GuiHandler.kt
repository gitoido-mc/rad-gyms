package lol.gito.radgyms.gui

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

@Environment(EnvType.CLIENT)
object GuiHandler {
    @Environment(EnvType.CLIENT)
    fun openGymKeyScreen(player: PlayerEntity) {
        MinecraftClient.getInstance().execute { MinecraftClient.getInstance().setScreen(GymEnterScreen(player)) }
    }
    @Environment(EnvType.CLIENT)
    fun openGymEntranceScreen(player: PlayerEntity, type: String, blockPos: BlockPos) {
        MinecraftClient.getInstance().execute { MinecraftClient.getInstance().setScreen(GymEnterScreen(player, type, blockPos)) }
    }
    @Environment(EnvType.CLIENT)
    fun openGymLeaveScreen(player: PlayerEntity) {
        MinecraftClient.getInstance().execute { MinecraftClient.getInstance().setScreen(GymLeaveScreen(player)) }
    }
}