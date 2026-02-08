/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.world.state

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.exception.RadGymsLevelNotFoundException
import lol.gito.radgyms.common.registry.RadGymsDimensions.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.util.getRadGymsPlayerData
import lol.gito.radgyms.common.util.putRadGymsPlayerData
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

            playersCompound.allKeys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                state.playerDataMap[uuid] = playersCompound.getRadGymsPlayerData(uuidString)
            }

            return state
        }

        @JvmStatic
        fun getServerState(server: MinecraftServer): RadGymsState {
            val level = server.getLevel(RADGYMS_LEVEL_KEY) ?:
                throw RadGymsLevelNotFoundException("Trying to access non-existing level")

            return level.dataStorage.computeIfAbsent(type, MOD_ID).also {
                it.setDirty()
            }
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

        @JvmStatic
        fun removeGymForPlayerByUuid(uuid: UUID) {
            RadGyms.implementation.server()?.let {
                debug("Removing gyms for player {}", uuid)
                getServerState(it).gymInstanceMap.remove(uuid)
            }
        }

        fun getPlayerUUIDForGym(gym: Gym): UUID {
            return getServerState(RadGyms.implementation.server()!!).gymInstanceMap.firstNotNullOf {
                if (it.value == gym) return@firstNotNullOf it.key
                return@firstNotNullOf null
            }
        }
    }

    override fun save(
        nbt: CompoundTag,
        registryLookup: HolderLookup.Provider
    ): CompoundTag {
        val playerDataNbt = CompoundTag()
        playerDataMap.forEach { (uuid, data) ->
            playerDataNbt.putRadGymsPlayerData(uuid.toString(), data)
        }

//        val gymDataNbt = CompoundTag()
//        gymInstanceMap.forEach { (uuid, data) ->
//            gymDataNbt.putRadGymsInstanceData(uuid.toString(), data)
//        }
//
//
//        nbt.put("Players", playerDataNbt)
//        nbt.put("Gyms", gymDataNbt)

        return nbt
    }
}