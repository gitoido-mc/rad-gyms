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
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructurePlacer
import lol.gito.radgyms.common.world.state.RadGymsState
import lol.gito.radgyms.common.world.state.dto.PlayerData
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType

class GymInitializer(
    private val templateRegistry: RadGymsTemplates,
    private val trainerSpawner: TrainerSpawner,
    private val structureManager: StructurePlacer,
    private val trainerFactory: TrainerFactory
) {
    fun initInstance(serverPlayer: ServerPlayer, serverWorld: ServerLevel, level: Int, type: String?): Boolean {
        val dto = templateRegistry.templates[type]
        val gymDimension = serverPlayer.server.getLevel(RadGymsDimensions.RADGYMS_LEVEL_KEY)

        if (dto == null || gymDimension == null) {
            return false
        }

        RadGymsState.setReturnCoordsForPlayer(
            serverPlayer,
            PlayerData.ReturnCoords(
                serverWorld.dimension().location(),
                serverPlayer.position().toBlockPos()
            )
        )

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
            gymDimension.getChunk(dest).pos,
            TELEPORT_PRELOAD_CHUNKS,
            dest
        )

        PlayerSpawnHelper.teleportPlayer(serverPlayer, gymDimension, dest, gymTemplate.playerYaw, 0.0F)

        val trainerUUIDs = trainerSpawner.spawnAll(gymTemplate, gymDimension, playerGymCoords)

        val label = "" // compute label similarly to previous logic
        val gymInstance = Gym(
            gymTemplate,
            trainerUUIDs,
            playerGymCoords,
            gymLevel,
            type ?: DEFAULT_GYM_TYPE,
            label
        )

        RadGymsState.addGymForPlayer(serverPlayer, gymInstance)

        return true
    }
}
