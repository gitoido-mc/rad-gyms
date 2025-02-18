package lol.gito.radgyms.gui

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.entity.player.PlayerEntity
import org.lwjgl.glfw.GLFW

class GymLeaveScreen(
    val player: PlayerEntity
) : BaseUIModelScreen<FlowLayout>(
    FlowLayout::class.java,
    DataSource.asset(RadGyms.modId("gym_leave_ui"))
) {
    private lateinit var root: FlowLayout

    override fun build(root: FlowLayout) {
        this.root = root
        root.childById(ButtonComponent::class.java, "ok").onPress {
            this.sendLeaveGymPacket()
        }
        root.childById(ButtonComponent::class.java, "cancel").onPress {
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
            RadGyms.LOGGER.info("Sending GymLeave C2S packet from ${player.name}")
            RadGyms.CHANNEL.clientHandle().send(NetworkStackHandler.GymLeave())
        } catch (e: Exception) {
            RadGyms.LOGGER.info(e.toString())
        }
    }
}