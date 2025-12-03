/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.gym

import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.util.getVec3d
import lol.gito.radgyms.common.util.putVec3d
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import lol.gito.radgyms.common.api.dto.TrainerModel as RGTrainerModel

data class GymTemplate(
    val structure: String,
    val relativeExitBlockSpawn: Vec3,
    val relativePlayerSpawn: Vec3,
    val playerYaw: Float,
    val trainers: List<RGTrainerModel>,
    val type: String?,
    val lootTables: List<Gym.Json.LootTableInfo>
) {
    companion object {
        fun fromDto(
            player: ServerPlayer,
            dto: Gym.Json,
            level: Int,
            type: String?,
            trainerFactory: TrainerFactory = TrainerFactory(),
            teamGenerator: TeamGenerator = TeamGenerator()
        ): GymTemplate {
            val trainers = dto.trainers.map {
                trainerFactory.create(it, type, level, player, teamGenerator)
            }

            return GymTemplate(
                structure = dto.template,
                relativeExitBlockSpawn = dto.exitBlockPos.toVec3D(),
                relativePlayerSpawn = dto.playerSpawnRelative.pos.toVec3D(),
                playerYaw = dto.playerSpawnRelative.yaw.toFloat(),
                trainers = trainers,
                type = type,
                lootTables = dto.rewardLootTables
            )
        }

        fun fromNbt(tag: CompoundTag): GymTemplate {
            // Basic primitive/vector decoding
            val structure = tag.getString("structure")
            val exit = tag.getVec3d("exit")
            val player = tag.getVec3d("player")
            val yaw = tag.getFloat("playerYaw")

            // TODO: deserialize trainers & lootTables using knbt or JSON fallback
            val trainers = emptyList<RGTrainerModel>()
            val lootTables = emptyList<Gym.Json.LootTableInfo>()

            return GymTemplate(structure, exit, player, yaw, trainers, tag.getString("type").ifBlank { null }, lootTables)
        }
    }

    fun toNbt(): CompoundTag {
        val tag = CompoundTag()
        tag.putString("structure", structure)
        tag.putVec3d("exit", relativeExitBlockSpawn)
        tag.putVec3d("player", relativePlayerSpawn)
        tag.putFloat("playerYaw", playerYaw)
        tag.putString("type", type ?: "")

        // TODO: serialize `trainers` and `lootTables` using knbt serialization helpers.
        // Fallback approach: convert complex objects to JSON strings and store in NBT string list.

        return tag
    }
}