/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

@file:Suppress("unused")

package lol.gito.radgyms.common.util

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.gym.GymTemplate
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.world.state.RadGymsState
import lol.gito.radgyms.common.world.state.dto.PlayerData
import lol.gito.radgyms.common.world.state.dto.PlayerData.ReturnCoords
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

fun CompoundTag.getBlockPos(key: String): BlockPos? {
    val nbt = this.getCompound(key) ?: return null

    requireNotNull(nbt.getInt("x"))
    requireNotNull(nbt.getInt("y"))
    requireNotNull(nbt.getInt("z"))

    return BlockPos(
        nbt.getInt("x"),
        nbt.getInt("y"),
        nbt.getInt("z"),
    )
}

fun CompoundTag.putBlockPos(key: String, value: BlockPos) {
    val nbt = CompoundTag()
    nbt.putInt("x", value.x)
    nbt.putInt("y", value.y)
    nbt.putInt("z", value.y)

    this.put(key, nbt)
}

fun CompoundTag.getVec3d(key: String): Vec3? {
    val nbt = this.getCompound(key) ?: return null

    requireNotNull(nbt.getDouble("x"))
    requireNotNull(nbt.getDouble("y"))
    requireNotNull(nbt.getDouble("z"))

    return Vec3(
        nbt.getDouble("x"),
        nbt.getDouble("y"),
        nbt.getDouble("z"),
    )
}

fun CompoundTag.putVec3d(key: String, value: Vec3) {
    val nbt = CompoundTag()
    nbt.putDouble("x", value.x)
    nbt.putDouble("y", value.y)
    nbt.putDouble("z", value.y)

    this.put(key, nbt)
}

fun CompoundTag.putRadGymsPlayerData(key: String, value: PlayerData) {
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

fun CompoundTag.getRadGymsPlayerData(key: String): PlayerData {
    val tag = this.getCompound(key)

    val playerData = PlayerData()

    if (tag.contains("Visits")) {
        playerData.visits = tag.getInt("Visits")
    }

    if (tag.contains("ReturnCoords")) {
        val returnCoords = tag.getCompound("ReturnCoords")

        if (returnCoords.contains("Position")) {
            playerData.returnCoords = ReturnCoords(
                ResourceLocation.parse(returnCoords.getString("Dimension")),
                returnCoords.getBlockPos("Position")!!
            )
        }
    }

    return playerData
}

fun CompoundTag.putRadGymsInstanceData(key: String, value: Gym) {
    val nbt = CompoundTag()
    val trainersNbt = CompoundTag()
    val playerUUID = RadGymsState.getPlayerUUIDForGym(value)

    nbt.putUUID("Player", playerUUID)
    nbt.putString("Template", value.type)
    nbt.putBlockPos("Coords", value.coords)
    nbt.putInt("Level", value.level)
    nbt.putString("Type", value.type)
    nbt.putString("Label", value.label)

    value.npcList.forEach { (uuid, model) ->
        trainersNbt.putRadGymsTrainerModel(uuid.toString(), model)
    }

    nbt.put("Trainers", trainersNbt)

    this.put(key, nbt)
}

fun CompoundTag.getRadGymsInstanceData(key: String): Gym {
    val tag = this.getCompound(key)
    val template = RadGymsTemplates.templates[tag.getString("Template")]!!
    val player = RadGyms.implementation.server()!!.playerList.getPlayer(
        tag.getUUID("Player")
    )!!

    return Gym(
        template = GymTemplate.fromDto(
            player,
            template,
            tag.getInt("Level"),
            tag.getString("Type")
        ),
        npcList = emptyMap(), // TODO: serialize trainers into nbt and fetch them back here
        coords = tag.getBlockPos("Coords")!!,
        level = tag.getInt("Level"),
        type = tag.getString("Type"),
        label = tag.getString("Label")
    )
}

fun CompoundTag.putRadGymsTrainerModel(key: String, value: TrainerModel) {
    val tag = this.getCompound(key)

    val _ = CompoundTag()

    tag.putString("Id", value.id)
    tag.putString("Format", value.format.serializedName)
    tag.putBoolean("Leader", value.leader)
    if (value.requires != null) {
        tag.putString("Requires", value.requires)
    }


    this.put(key, tag)
}