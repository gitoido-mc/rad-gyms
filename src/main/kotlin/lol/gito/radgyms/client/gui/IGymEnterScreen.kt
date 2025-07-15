/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

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
