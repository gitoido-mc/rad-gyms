package lol.gito.radgyms.client.gui

import io.wispforest.owo.ui.container.FlowLayout

interface IGymEnterScreen {
    var gymLevel: Double
    var root: FlowLayout

    fun incPress(value: Double)
    fun decPress(value: Double)
    fun sendStartGymPacket()
    fun updateSlider()
    fun close()
}
