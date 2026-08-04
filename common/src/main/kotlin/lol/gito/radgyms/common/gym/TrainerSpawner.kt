/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.bedrockk.molang.runtime.value.DoubleValue
import com.bedrockk.molang.runtime.value.StringValue
import com.cobblemon.mod.common.api.npc.NPCClasses
import com.cobblemon.mod.common.entity.npc.NPCEntity
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.trainer.TrainerModel
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import java.util.UUID

object TrainerSpawner {
    fun spawnAll(template: GymTemplate, gymDimension: ServerLevel, coords: BlockPos): Map<UUID, TrainerModel> {
        val trainerIds = mutableMapOf<String, Pair<UUID, TrainerModel>>()

        template.trainers.forEach { trainer ->
            val requiredUUID = trainer.requires?.let { trainerIds[it]?.first }
            val pair = buildTrainerEntity(trainer, gymDimension, coords, requiredUUID)
            trainerIds[trainer.id] = pair
        }

        return trainerIds.values.associate { it.first to it.second }
    }

    private fun buildTrainerEntity(
        trainer: TrainerModel,
        gymDimension: ServerLevel,
        coords: BlockPos,
        requiredUUID: UUID?,
    ): Pair<UUID, TrainerModel> {
        val npcClass =
            NPCClasses.getByIdentifier(modId(trainer.id)) ?: error("Cannot find NPC with id: ${modId(trainer.id)}")

        val npc = NPCEntity(gymDimension)

        npc.config.map.putIfAbsent("defeated", DoubleValue.ZERO)

        if (requiredUUID != null) {
            npc.config.map.putIfAbsent("required_trainer", StringValue(requiredUUID.toString()))
        }

        if (trainer.leader) {
            npc.config.map.putIfAbsent("leader", DoubleValue.ONE)
        }

        npc.isNoGravity = true

        npc.moveTo(
            coords.x + trainer.npc.relativePosition.x,
            coords.y + trainer.npc.relativePosition.y,
            coords.z + trainer.npc.relativePosition.z,
            trainer.npc.yaw,
            npc.xRot,
        )
        npc.setYBodyRot(trainer.npc.yaw)
        npc.npc = npcClass
        npc.initialize(trainer.trainer.team.first().level)
        gymDimension.addFreshEntity(npc)

        debug("Spawned trainer ${npc.id} at ${npc.x} ${npc.y} ${npc.z}")

        return Pair(npc.uuid, trainer)
    }
}
