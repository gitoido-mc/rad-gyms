package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.state.PlayerData
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructurePlacer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType

class GymInitializer(
    private val templateRegistry: RadGymsTemplates,
    private val trainerSpawner: TrainerSpawner,
    private val structureManager: StructurePlacer,
    private val trainerFactory: TrainerFactory,
    private val teamGenerator: TeamGenerator
) {
    fun initInstance(serverPlayer: ServerPlayer, serverWorld: ServerLevel, level: Int, type: String?): Boolean {
        val dto = templateRegistry.getTemplateOrDefault(type) ?: return false

        RadGymsState.setReturnCoordsForPlayer(
            serverPlayer,
            PlayerData.ReturnCoords(
                serverWorld.dimension().location(),
                serverPlayer.position().toBlockPos()
            )
        )

        val gymLevel = level.coerceIn(5..Cobblemon.config.maxPokemonLevel)
        val gymType = if (type in templateRegistry.getTemplateIdentifiers()) type else "default"

        val gymTemplate = GymTemplate.fromDto(serverPlayer, dto, gymLevel, gymType, trainerFactory, teamGenerator)

        val gymDimension = serverPlayer.server!!.getLevel(RadGymsDimensions.RADGYMS_LEVEL_KEY) ?: return false
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
            4,
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
            type ?: "default",
            label
        )

        RadGymsState.addGymForPlayer(serverPlayer, gymInstance)

        // register to RCT registry (keep same behavior)
        val trainerRegistry = RadGyms.RCT.trainerRegistry
        gymInstance.npcList.forEach { (uuid, npc) ->
            val trainer = trainerRegistry.registerNPC(uuid.toString(), npc.trainer)
            trainer.entity = gymDimension.getEntity(uuid) as Trainer
        }

        return true
    }
}