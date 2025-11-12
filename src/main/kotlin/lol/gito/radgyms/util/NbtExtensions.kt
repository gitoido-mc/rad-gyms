/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.util

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

fun NbtCompound.getBlockPos(key: String): BlockPos {
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

fun NbtCompound.putBlockPos(key: String, value: BlockPos) {
    val nbt = NbtCompound()
    nbt.putInt("x", value.x)
    nbt.putInt("y", value.y)
    nbt.putInt("z", value.y)

    this.put(key, nbt)
}

fun NbtCompound.getVec3d(key: String): Vec3d {
    assert(this.contains(key))

    val nbt = this.getCompound(key)
    assert(nbt.contains("x"))
    assert(nbt.contains("y"))
    assert(nbt.contains("z"))

    return Vec3d(
        nbt.getDouble("x"),
        nbt.getDouble("y"),
        nbt.getDouble("z"),
    )
}

fun NbtCompound.putVec3d(key: String, value: Vec3d) {
    val nbt = NbtCompound()
    nbt.putDouble("x", value.x)
    nbt.putDouble("y", value.y)
    nbt.putDouble("z", value.y)

    this.put(key, nbt)
}