/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.state

import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.registry.RadGymsDimensions.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.mixin.util.accessor.RCTBattleAIAccessor
import lol.gito.radgyms.common.util.getBlockPos
import lol.gito.radgyms.common.util.putBlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class RadGymsState : SavedData() {
    val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
    val gymInstanceMap: MutableMap<UUID, Gym> = mutableMapOf()

    companion object {
        fun create() = RadGymsState()

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        private val type: Factory<RadGymsState> = Factory(
            ::create,
            ::save,
            null
        )

        @Suppress("unused")
        fun save(
            nbt: CompoundTag,
            registryLookup: HolderLookup.Provider
        ): RadGymsState {
            val state = RadGymsState()

            nbt.getCompound("Players").allKeys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                val playerDataNbt = nbt.getCompound("Players").getCompound(uuidString)
                val playerData = PlayerData(playerDataNbt.getInt("Visits"))

                if (playerDataNbt.contains("ReturnCoords")) {
                    val returnCoords = playerDataNbt.getCompound("ReturnCoords")
                    playerData.returnCoords = PlayerData.ReturnCoords(
                        ResourceLocation.parse(returnCoords.getString("Dimension")),
                        returnCoords.getBlockPos("Position")
                    )
                }

                state.playerDataMap[uuid] = playerData
            }

            return state
        }

        fun getServerState(server: MinecraftServer): RadGymsState {
            val stateManager = server.getLevel(RADGYMS_LEVEL_KEY)!!.dataStorage
            return stateManager.computeIfAbsent(type, MOD_ID).also {
                debug("marking server state as dirty")
                it.setDirty()
            }
        }

        fun getPlayerState(player: Player): PlayerData {
            val serverState = getServerState(player.server!!)

            return serverState.playerDataMap.computeIfAbsent(player.uuid) { PlayerData() }
        }

        fun incrementVisitsForPlayer(player: ServerPlayer) {
            getPlayerState(player).incrementVisits()
        }

        fun setReturnCoordsForPlayer(player: ServerPlayer, coords: PlayerData.ReturnCoords?) {
            getPlayerState(player).returnCoords = coords
        }

        fun hasGymForPlayer(player: ServerPlayer): Boolean {
            return getServerState(player.server).gymInstanceMap.keys.contains(player.uuid)
        }

        fun getGymForPlayer(player: ServerPlayer): Gym? {
            return getServerState(player.server).gymInstanceMap[player.uuid]
        }

        fun addGymForPlayer(player: ServerPlayer, gymInstance: Gym) {
            if (hasGymForPlayer(player)) {
                removeGymForPlayer(player)
            }

            getServerState(player.server).gymInstanceMap[player.uuid] = gymInstance
        }

        fun removeGymForPlayer(player: ServerPlayer) {
            getServerState(player.server).gymInstanceMap.remove(player.uuid)
        }
    }

    override fun save(
        nbt: CompoundTag,
        registryLookup: HolderLookup.Provider
    ): CompoundTag {
        val playerDataNbt = CompoundTag()
        playerDataMap.forEach { (uuid, data) ->
            val playerData = CompoundTag()
            playerData.putInt("Visits", data.visits)
            if (data.returnCoords != null) {
                val returnCoordsNbt = CompoundTag()
                returnCoordsNbt.putString("Dimension", data.returnCoords!!.dimension.toString())
                returnCoordsNbt.putBlockPos("Position", data.returnCoords!!.position)
                playerData.put("ReturnCoords", returnCoordsNbt)
            }

            playerDataNbt.put(uuid.toString(), playerData)
        }

        nbt.put("Players", playerDataNbt)

        return nbt
    }

    /**
     * TODO: Persistence between restarts
     */
    @Suppress("unused")
    private fun gymCompoundTag(): CompoundTag {
        val trainerRegistry = RadGyms.RCT.trainerRegistry
        val gymCompoundTag = CompoundTag()

        gymInstanceMap.forEach { (gymUuid, instance) ->
            val gymData = CompoundTag()
            gymData.putInt("Level", instance.level)
            gymData.putString("Type", instance.type)
            gymData.putString("Label", instance.label)
            gymData.putBlockPos("Coords", instance.coords)

            val gymTrainers = CompoundTag()

            instance.npcList.forEach { (uuid, npc) ->
                val trainerNbt = CompoundTag()
                val trainer = trainerRegistry.getById(uuid.toString(), TrainerNPC::class.java)
                val trainerBagNbt = CompoundTag()
                val trainerTeamNbt = CompoundTag()
                val trainerAiNbt = CompoundTag()

                (trainer.battleAI as RCTBattleAIAccessor).let {
                    trainerAiNbt.putDouble("moveBias", it.moveBias)
                    trainerAiNbt.putDouble("statusMoveBias", it.statusMoveBias)
                    trainerAiNbt.putDouble("switchBias", it.switchBias)
                    trainerAiNbt.putDouble("itemBias", it.itemBias)
                    trainerAiNbt.putDouble("maxSelectMargin", it.maxSelectMargin)
                }

                trainer.bag.items.forEach { bagItem ->
                    trainerBagNbt.putInt(bagItem.itemName, trainer.bag.getQuanity(bagItem))
                }

                trainer.team.forEachIndexed { index, pokemon ->
                    trainerTeamNbt.putString(
                        index.toString(),
                        pokemon.createPokemonProperties(PokemonPropertyExtractor.ALL).asString()
                    )
                }


                trainerNbt.putString("Name", trainer.name.literal)
                trainerNbt.putString("TrainerId", npc.id)
                trainerNbt.put("Bag", trainerBagNbt)
                trainerNbt.put("Team", trainerTeamNbt)
                gymTrainers.put(uuid.toString(), trainerNbt)
            }
            gymData.put("Trainers", gymTrainers)
            gymCompoundTag.put(gymUuid.toString(), gymData)
        }

        return gymCompoundTag
    }
}