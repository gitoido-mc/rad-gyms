/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.world.state

import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.api.dto.gym.Gym
import lol.gito.radgyms.common.exception.RadGymsLevelNotFoundException
import lol.gito.radgyms.common.extension.nbt.getRadGymsInstanceData
import lol.gito.radgyms.common.extension.nbt.getRadGymsPlayerData
import lol.gito.radgyms.common.extension.nbt.putRadGymsInstanceData
import lol.gito.radgyms.common.extension.nbt.putRadGymsPlayerData
import lol.gito.radgyms.common.registry.RadGymsDimensions.GYM_DIMENSION
import lol.gito.radgyms.common.world.state.dto.PlayerData
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class RadGymsState : SavedData() {
    val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
    val gymInstanceMap: MutableMap<UUID, Gym> = mutableMapOf()

    @Suppress("TooManyFunctions")
    companion object {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        private val type: Factory<RadGymsState> = Factory(
            ::RadGymsState,
            ::load,
            null
        )

        @Suppress("unused")
        @JvmStatic
        fun load(
            nbt: CompoundTag,
            registryLookup: HolderLookup.Provider
        ): RadGymsState {
            val state = RadGymsState()
            val playersCompound = when (nbt.contains("Players")) {
                true -> nbt.getCompound("Players")
                false -> CompoundTag()
            }
            val gymsCompound = when (nbt.contains("Gyms")) {
                true -> nbt.getCompound("Gyms")
                false -> CompoundTag()
            }

            playersCompound.allKeys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                state.playerDataMap.putIfAbsent(uuid, playersCompound.getRadGymsPlayerData(uuidString)!!)
            }
            gymsCompound.allKeys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                state.gymInstanceMap.putIfAbsent(uuid, gymsCompound.getRadGymsInstanceData(uuidString)!!)
            }

            return state
        }

        @JvmStatic
        fun getServerState(server: MinecraftServer): RadGymsState {
            val level = server.getLevel(GYM_DIMENSION)
                ?: throw RadGymsLevelNotFoundException("Trying to access non-existing level")

            return level.dataStorage.computeIfAbsent(type, MOD_ID).also {
                it.setDirty()
            }
        }

        @JvmStatic
        fun markDirty(player: ServerPlayer) {
            val level = player.server.getLevel(GYM_DIMENSION)
                ?: throw RadGymsLevelNotFoundException("Trying to access non-existing level")

            level.dataStorage.computeIfAbsent(type, MOD_ID).setDirty()
        }

        @JvmStatic
        fun getPlayerState(player: ServerPlayer): PlayerData {
            val serverState = getServerState(player.server)

            return serverState.playerDataMap.computeIfAbsent(player.uuid) { PlayerData() }
        }

        @JvmStatic
        fun incrementVisitsForPlayer(player: ServerPlayer) {
            getPlayerState(player).incrementVisits()
        }

        @JvmStatic
        fun setReturnCoordsForPlayer(player: ServerPlayer, coords: PlayerData.ReturnCoords?) {
            getPlayerState(player).returnCoords = coords
        }

        @JvmStatic
        fun hasGymForPlayer(player: ServerPlayer): Boolean {
            return getServerState(player.server).gymInstanceMap.keys.contains(player.uuid)
        }

        @JvmStatic
        fun getGymForPlayer(player: ServerPlayer): Gym? {
            return getServerState(player.server).gymInstanceMap[player.uuid]
        }

        @JvmStatic
        fun addGymForPlayer(player: ServerPlayer, gymInstance: Gym) {
            if (hasGymForPlayer(player)) {
                removeGymForPlayer(player)
            }

            getServerState(player.server).gymInstanceMap[player.uuid] = gymInstance
        }

        @JvmStatic
        fun removeGymForPlayer(player: ServerPlayer) {
            getServerState(player.server).gymInstanceMap.remove(player.uuid)
        }
    }

    override fun save(
        nbt: CompoundTag,
        registryLookup: HolderLookup.Provider
    ): CompoundTag {
        val playerDataNbt = CompoundTag()
        val gymDataNbt = CompoundTag()

        playerDataMap.forEach { (uuid, data) ->
            playerDataNbt.putRadGymsPlayerData(uuid.toString(), data)
        }
        gymInstanceMap.forEach { (uuid, data) ->
            gymDataNbt.putRadGymsInstanceData(uuid.toString(), data)
        }

        nbt.put("Players", playerDataNbt)
        nbt.put("Gyms", gymDataNbt)

        return nbt
    }
}
