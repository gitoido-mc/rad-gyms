/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import lol.gito.radgyms.common.api.dto.trainer.TrainerModel as RGTrainerModel

data class GymTemplate(
    val id: String,
    val structure: String,
    val relativeExitBlockSpawn: Vec3,
    val relativePlayerSpawn: Vec3,
    val playerYaw: Float,
    val trainers: List<RGTrainerModel>,
    val type: String?,
    val rewards: List<RewardInterface>
) {
    companion object {
        fun fromDto(
            player: ServerPlayer?,
            dto: GymJson,
            level: Int,
            type: String?,
            trainerFactory: TrainerFactory = TrainerFactory()
        ): GymTemplate {
            val trainers = dto.trainers.map {
                trainerFactory.create(it, level, player)
            }

            return GymTemplate(
                id = dto.id,
                structure = dto.template,
                relativeExitBlockSpawn = dto.exitBlockPos.toVec3D(),
                relativePlayerSpawn = dto.playerSpawnRelative.pos.toVec3D(),
                playerYaw = dto.playerSpawnRelative.yaw.toFloat(),
                trainers = trainers,
                type = type,
                rewards = dto.rewards
            )
        }
    }
}
