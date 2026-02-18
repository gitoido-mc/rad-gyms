/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.trainer.TrainerConfiguration
import lol.gito.radgyms.common.api.dto.trainer.TrainerModel
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.RadGymsEntities
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import java.util.*

object TrainerSpawner {
    fun spawnAll(template: GymTemplate, gymDimension: ServerLevel, coords: BlockPos): Map<UUID, TrainerModel> {
        val trainerIds = mutableMapOf<String, Pair<UUID, TrainerModel>>()

        template.trainers.forEach { trainer ->
            val uuid = UUID.randomUUID()
            val requiredUUID = trainer.requires?.let { trainerIds[it]?.first }
            val pair = buildTrainerEntity(trainer, gymDimension, coords, uuid, requiredUUID)
            trainerIds[trainer.id] = pair
        }

        return trainerIds.values.associate { it.first to it.second }
    }

    private fun buildTrainerEntity(
        trainer: TrainerModel,
        gymDimension: ServerLevel,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ): Pair<UUID, TrainerModel> {
        val trainerEntity = Trainer(RadGymsEntities.GYM_TRAINER, gymDimension).apply {
            uuid = trainerUUID
            gymId = trainer.id
            leader = trainer.leader
            format = trainer.format.name
            trainerId = trainerUUID
            requires = requiredUUID
            yHeadRot = trainer.npc.yaw
            yBodyRot = trainer.npc.yaw
            customName = trainer.npc.name
            configuration = TrainerConfiguration(
                trainer.battleRules,
                trainer.trainer.bag,
                trainer.trainer.team
            )
            isCustomNameVisible = true
            setPersistenceRequired()
            setPos(
                Vec3(
                    coords.x + trainer.npc.relativePosition.x,
                    coords.y + trainer.npc.relativePosition.y,
                    coords.z + trainer.npc.relativePosition.z
                )
            )
        }

        debug("Spawning trainer ${trainerEntity.id} at ${trainerEntity.x} ${trainerEntity.y} ${trainerEntity.z}")
        gymDimension.tryAddFreshEntityWithPassengers(trainerEntity)

        return Pair(trainerEntity.uuid, trainer)
    }
}
