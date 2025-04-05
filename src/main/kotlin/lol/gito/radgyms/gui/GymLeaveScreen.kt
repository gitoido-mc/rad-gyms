package lol.gito.radgyms.gui

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.gui.GymGUIIdentifiers.ID_CANCEL
import lol.gito.radgyms.gui.GymGUIIdentifiers.ID_OK
import lol.gito.radgyms.gui.GymGUIIdentifiers.UI_GYM_LEAVE
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.entity.player.PlayerEntity
import org.lwjgl.glfw.GLFW

class GymLeaveScreen(
    val player: PlayerEntity
) : BaseUIModelScreen<FlowLayout>(
    FlowLayout::class.java,
    DataSource.asset(UI_GYM_LEAVE)
) {
    private lateinit var root: FlowLayout

    override fun build(root: FlowLayout) {
        this.root = root
        root.childById(ButtonComponent::class.java, ID_OK).onPress {
            this.sendLeaveGymPacket()
        }
        root.childById(ButtonComponent::class.java, ID_CANCEL).onPress {
            this.close()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun sendLeaveGymPacket() {
        try {
            this.close()
            debug("Sending GymLeave C2S packet from ${player.name}")
            CHANNEL.clientHandle().send(NetworkStackHandler.GymLeave(teleport = true))
        } catch (e: Exception) {
            LOGGER.info(e.toString())
        }
    }
}
