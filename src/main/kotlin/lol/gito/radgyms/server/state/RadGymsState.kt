/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.state

import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.gym.GymInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*

class RadGymsState : PersistentState() {
    val playerDataMap: HashMap<UUID, PlayerData> = hashMapOf()
    val gymInstanceMap: HashMap<UUID, GymInstance> = hashMapOf()

    companion object {
        fun create() = RadGymsState()

        private val type: Type<RadGymsState> = Type(
            ::create,
            ::readNbt,
            null
        )

        @Suppress("unused")
        fun readNbt(
            nbt: NbtCompound,
            registryLookup: RegistryWrapper.WrapperLookup
        ): RadGymsState {
            val state = RadGymsState()

            nbt.getCompound("Players").keys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                val playerDataNbt = nbt.getCompound(uuidString)
                val playerData = PlayerData(
                    nbt.getCompound(uuidString).getInt("Visits")
                )

                if (playerDataNbt.contains("ReturnCoords")) {
                    val returnCoords = playerDataNbt.getCompound("ReturnCoords")
                    playerData.returnCoords = PlayerData.ReturnCoords(
                        Identifier.of(returnCoords.getString("Dimension")),
                        BlockPos.fromLong(returnCoords.getLong("Position"))
                    )
                }

                state.playerDataMap.put(uuid, playerData)
            }

            return state
        }

        fun getServerState(server: MinecraftServer): RadGymsState {
            val stateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
            return stateManager.getOrCreate(type, MOD_ID).also {
                debug("marking server state as dirty")
                it.markDirty()
            }
        }

        fun getPlayerState(player: PlayerEntity): PlayerData {
            val serverState = getServerState(player.world.server!!)

            return serverState.playerDataMap.computeIfAbsent(player.uuid) { PlayerData() }
        }

        fun incrementVisitsForPlayer(player: ServerPlayerEntity) {
            getPlayerState(player).incrementVisits()
        }

        fun setReturnCoordsForPlayer(player: ServerPlayerEntity, coords: PlayerData.ReturnCoords?) {
            getPlayerState(player).returnCoords = coords
        }
    }

    override fun writeNbt(
        nbt: NbtCompound,
        registryLookup: RegistryWrapper.WrapperLookup
    ): NbtCompound {
        val playerDataNbt = NbtCompound()
        playerDataMap.forEach { (uuid, data) ->
            val playerData = NbtCompound()
            playerData.putInt("Visits", data.visits)
            if (data.returnCoords != null) {
                val returnCoordsNbt = NbtCompound()
                returnCoordsNbt.putString("Dimension", data.returnCoords!!.dimension.toString())
                returnCoordsNbt.putLong("Position", data.returnCoords!!.position.asLong())
                playerData.put("ReturnCoords", returnCoordsNbt)
            }

            playerDataNbt.put(uuid.toString(), playerData)
        }

        nbt.put("Players", playerDataNbt)
        return nbt
    }
}