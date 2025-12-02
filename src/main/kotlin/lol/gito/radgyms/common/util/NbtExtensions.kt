/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

@file:Suppress("unused")

package lol.gito.radgyms.common.util

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

fun CompoundTag.getBlockPos(key: String): BlockPos {
    assert(this.contains(key))
    val nbt = this.getCompound(key)!!

    assert(nbt.contains("x"))
    assert(nbt.contains("y"))
    assert(nbt.contains("z"))

    return BlockPos(
        nbt.getInt("x"),
        nbt.getInt("y"),
        nbt.getInt("z"),
    )
}

fun CompoundTag.putBlockPos(key: String, value: BlockPos) {
    val nbt = CompoundTag()
    nbt.putInt("x", value.x)
    nbt.putInt("y", value.y)
    nbt.putInt("z", value.y)

    this.put(key, nbt)
}

fun CompoundTag.getVec3d(key: String): Vec3 {
    assert(this.contains(key))

    val nbt = this.getCompound(key)
    assert(nbt.contains("x"))
    assert(nbt.contains("y"))
    assert(nbt.contains("z"))

    return Vec3(
        nbt.getDouble("x"),
        nbt.getDouble("y"),
        nbt.getDouble("z"),
    )
}

fun CompoundTag.putVec3d(key: String, value: Vec3) {
    val nbt = CompoundTag()
    nbt.putDouble("x", value.x)
    nbt.putDouble("y", value.y)
    nbt.putDouble("z", value.y)

    this.put(key, nbt)
}