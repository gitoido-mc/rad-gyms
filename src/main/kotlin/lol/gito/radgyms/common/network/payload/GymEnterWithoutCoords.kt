/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.payload

import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

@JvmRecord
data class GymEnterWithoutCoords(
    val id: Identifier = modId("net.gym_enter_key"),
    val level: Int,
    val type: String
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = CustomPayload.Id(this.id)
}