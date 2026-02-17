/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.common.DEFAULT_GYM_TYPE
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.TELEPORT_PRELOAD_CHUNKS
import lol.gito.radgyms.common.api.dto.gym.Gym
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GYM_ENTER
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.registry.RadGymsStats.getStat
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructurePlacer
import lol.gito.radgyms.common.world.state.RadGymsState
import lol.gito.radgyms.common.world.state.dto.PlayerData
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType
import net.minecraft.world.level.ChunkPos

class GymInitializer(
    private val templateRegistry: RadGymsTemplates,
    private val trainerSpawner: TrainerSpawner,
    private val structureManager: StructurePlacer,
    private val trainerFactory: TrainerFactory
) {
    fun initInstance(
        serverPlayer: ServerPlayer,
        serverWorld: ServerLevel,
        level: Int,
        type: String = DEFAULT_GYM_TYPE,
        usedKey: Boolean = false
    ) {
        val dto = templateRegistry.templates[type]
        val gymDimension = serverPlayer.server.getLevel(RadGymsDimensions.GYM_DIMENSION)

        if (dto == null || gymDimension == null) return

        RadGymsState.incrementVisitsForPlayer(serverPlayer)
        RadGymsState.setReturnCoordsForPlayer(
            serverPlayer,
            PlayerData.ReturnCoords(
                serverWorld.dimension().location(),
                serverPlayer.position().toBlockPos()
            )
        )
        RadGymsState.markDirty(serverPlayer)

        val gymLevel = level.coerceIn(RadGyms.CONFIG.minLevel!!..RadGyms.CONFIG.maxLevel!!)
        val gymType = if (type in templateRegistry.templates.keys) type else DEFAULT_GYM_TYPE

        val gymTemplate = GymTemplate.fromDto(serverPlayer, dto, gymLevel, gymType, trainerFactory)

        val playerGymCoords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)
        val dest = BlockPos.containing(
            playerGymCoords.x + gymTemplate.relativePlayerSpawn.x,
            playerGymCoords.y + gymTemplate.relativePlayerSpawn.y,
            playerGymCoords.z + gymTemplate.relativePlayerSpawn.z
        )

        structureManager.placeStructure(gymDimension, playerGymCoords, gymTemplate.structure)
        gymDimension.chunkSource.addRegionTicket(
            TicketType.PORTAL,
            ChunkPos(dest),
            TELEPORT_PRELOAD_CHUNKS,
            dest
        )

        val trainers = trainerSpawner.spawnAll(gymTemplate, gymDimension, playerGymCoords)

        val gymInstance = Gym(
            gymTemplate,
            trainers.keys.toList(),
            playerGymCoords,
            gymLevel,
            type
        )

        RadGymsState.addGymForPlayer(serverPlayer, gymInstance)

        GYM_ENTER.emit(
            GymEvents.GymEnterEvent(
                serverPlayer,
                gymInstance,
                type,
                gymLevel,
                usedKey
            )
        )

        serverPlayer.awardStat(getStat(RadGyms.statistics.GYMS_VISITED))

        PlayerSpawnHelper.teleportPlayer(serverPlayer, gymDimension, dest, gymTemplate.playerYaw, 0.0F)
    }
}
