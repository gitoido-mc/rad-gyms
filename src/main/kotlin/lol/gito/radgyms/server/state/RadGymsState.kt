/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.state

import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.gym.GymInstance
import lol.gito.radgyms.common.gym.GymManager.GYM_TEMPLATES
import lol.gito.radgyms.common.gym.GymTemplate
import lol.gito.radgyms.common.gym.GymTrainer
import lol.gito.radgyms.common.registry.DimensionRegistry.RADGYMS_LEVEL_KEY
import lol.gito.radgyms.common.util.getBlockPos
import lol.gito.radgyms.common.util.putBlockPos
import lol.gito.radgyms.mixin.common.RCTBattleAIAccessor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import java.util.*

class RadGymsState : PersistentState() {
    val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
    val gymInstanceMap: MutableMap<UUID, GymInstance> = mutableMapOf()

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
                val playerDataNbt = nbt.getCompound("Players").getCompound(uuidString)
                val playerData = PlayerData(playerDataNbt.getInt("Visits"))

                if (playerDataNbt.contains("ReturnCoords")) {
                    val returnCoords = playerDataNbt.getCompound("ReturnCoords")
                    playerData.returnCoords = PlayerData.ReturnCoords(
                        Identifier.of(returnCoords.getString("Dimension")),
                        returnCoords.getBlockPos("Position")
                    )
                }

                state.playerDataMap.put(uuid, playerData)
            }

            nbt.getCompound("Gyms").keys.forEach { uuidString ->
                val uuid = UUID.fromString(uuidString)
                val gymDataNbt = nbt.getCompound("Gyms").getCompound(uuidString)
                val gymTemplate = GYM_TEMPLATES[gymDataNbt.getString("Type")]?.let {
                    GymTemplate.fromGymDto(
                        it,
                        gymDataNbt.getInt("Level"),
                        gymDataNbt.getString("Type")
                    )
                }!!

                val gymTrainers = mutableMapOf<UUID, GymTrainer>()
                gymDataNbt.getCompound("Trainers").keys.forEach { entityUuidString ->
                    val trainerUuid = UUID.fromString(uuidString)
                    val trainerDataNbt = gymDataNbt.getCompound("Trainers").getCompound(entityUuidString)
                    val trainerTemplateId = trainerDataNbt.getString("TrainerId")
                    val trainerTemplate = gymTemplate.trainers.first { trainer -> trainer.id == trainerTemplateId }
//                    val team = mutableListOf<PokemonModel>()
//
//
//                    val gymTrainer = GymTrainer(
//                        trainerTemplateId,
//                        trainerTemplate.npc,
//                        battleRules = TODO(),
//                        leader = TODO(),
//                        requires = TODO()
//                    )
//
//                    gymTrainers[trainerUuid] = gymTrainer
                }

                val gymInstance = GymInstance(
                    template = gymTemplate,
                    npcList = mutableMapOf(),
                    coords = gymDataNbt.getBlockPos("Coords"),
                    level = gymDataNbt.getInt("Level"),
                    type = gymDataNbt.getString("Type"),
                    label = gymDataNbt.getString("Label")
                )

                state.gymInstanceMap.put(uuid, gymInstance)
            }

            return state
        }

        fun getServerState(server: MinecraftServer): RadGymsState {
            val stateManager = server.getWorld(RADGYMS_LEVEL_KEY)!!.persistentStateManager
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

        fun hasGymForPlayer(player: ServerPlayerEntity): Boolean {
            return getServerState(player.server).gymInstanceMap.keys.contains(player.uuid)
        }

        fun getGymForPlayer(player: ServerPlayerEntity): GymInstance? {
            return getServerState(player.server).gymInstanceMap[player.uuid]
        }

        fun addGymForPlayer(player: ServerPlayerEntity, gymInstance: GymInstance) {
            if (hasGymForPlayer(player)) {
                removeGymForPlayer(player)
            }

            getServerState(player.server).gymInstanceMap.put(player.uuid, gymInstance)
        }

        fun removeGymForPlayer(player: ServerPlayerEntity) {
            getServerState(player.server).gymInstanceMap.remove(player.uuid)
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
                returnCoordsNbt.putBlockPos("Position", data.returnCoords!!.position)
                playerData.put("ReturnCoords", returnCoordsNbt)
            }

            playerDataNbt.put(uuid.toString(), playerData)
        }
        val gymDataNbt = gymNbtCompound()

        nbt.put("Players", playerDataNbt)
        nbt.put("Gyms", gymDataNbt)
        return nbt
    }

    private fun gymNbtCompound(): NbtCompound {
        val trainerRegistry = RadGyms.RCT.trainerRegistry
        val gymNbtCompound = NbtCompound()

        gymInstanceMap.forEach { (gymUuid, instance) ->
            val gymData = NbtCompound()
            gymData.putInt("Level", instance.level)
            gymData.putString("Type", instance.type)
            gymData.putString("Label", instance.label)
            gymData.putBlockPos("Coords", instance.coords)

            val gymTrainers = NbtCompound()

            instance.npcList.forEach { (uuid, npc) ->
                val trainerNbt = NbtCompound()
                val trainer = trainerRegistry.getById(uuid.toString(), TrainerNPC::class.java)
                val trainerBagNbt = NbtCompound()
                val trainerTeamNbt = NbtCompound()
                val trainerAiNbt = NbtCompound()

                // frick my chungus life
                ((trainer.battleAI as RCTBattleAI) as RCTBattleAIAccessor).let {
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
                debug("get data")
            }
            gymData.put("Trainers", gymTrainers)
            gymNbtCompound.put(gymUuid.toString(), gymData)
        }

        return gymNbtCompound
    }
}