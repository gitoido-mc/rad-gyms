/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.state

import lol.gito.radgyms.common.gym.GymInstance
import lol.gito.radgyms.common.gym.GymLootTable
import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

fun GymInstance.toNbtCompound(): NbtCompound {
    val nbt = NbtCompound()

    return nbt
}

fun GymTemplate.fromNbtCompound(nbt: NbtCompound): GymTemplate {
    this.structure = nbt.getString("Structure")
    this.lootTables = emptyList<GymLootTable>().also { list ->
        nbt.getCompound("LootTables").keys.forEach { lootTableIndex ->
            val lootTableNbt = nbt.getCompound("LootTables").getCompound(lootTableIndex)
            list.plus(
                GymLootTable(
                    Identifier.of(lootTableNbt.getString("Identifier")),
                    Pair(lootTableNbt.getInt("LevelMin"), lootTableNbt.getInt("LevelMax"))
                )
            )
        }
    }
    val relativeExitBlockSpawnNbt = nbt.getCompound("RelativeExitBlockSpawn")
    this.relativeExitBlockSpawn = Vec3d(
        relativeExitBlockSpawnNbt.getDouble("X"),
        relativeExitBlockSpawnNbt.getDouble("Y"),
        relativeExitBlockSpawnNbt.getDouble("Z")
    )
    val relativePlayerSpawnNbt = nbt.getCompound("RelativePlayerSpawn")
    this.relativePlayerSpawn = Vec3d(
        relativePlayerSpawnNbt.getDouble("X"),
        relativePlayerSpawnNbt.getDouble("Y"),
        relativePlayerSpawnNbt.getDouble("Z")
    )
    this.playerYaw = nbt.getFloat("PlayerYaw")

    return this
}
