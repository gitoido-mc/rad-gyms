/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.nbt

import lol.gito.radgyms.common.world.state.dto.PlayerData
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

fun CompoundTag.getRadGymsPlayerData(key: String): PlayerData? {
    val tag = this.getCompound(key) ?: return null

    val playerData = PlayerData()

    if (tag.contains("Visits")) playerData.visits = tag.getInt("Visits")
    if (tag.contains("ReturnCoords")) {
        val returnCoords = tag.getCompound("ReturnCoords")
        if (returnCoords.contains("Position")) {
            playerData.returnCoords =
                PlayerData.ReturnCoords(
                    ResourceLocation.parse(returnCoords.getString("Dimension")),
                    returnCoords.getBlockPos("Position")!!,
                )
        }
    }

    return playerData
}

fun CompoundTag.putRadGymsPlayerData(
    key: String,
    value: PlayerData,
) {
    val nbt = CompoundTag()

    nbt.putInt("Visits", value.visits)

    value.returnCoords?.let {
        val returnCoordsNbt = CompoundTag()
        returnCoordsNbt.putString("Dimension", it.dimension.toString())
        returnCoordsNbt.putBlockPos("Position", it.position)
        nbt.put("ReturnCoords", returnCoordsNbt)
    }
    this.put(key, nbt)
}
