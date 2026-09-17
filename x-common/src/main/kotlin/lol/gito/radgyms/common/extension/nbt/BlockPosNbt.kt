/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.nbt

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getBlockPos(key: String): BlockPos? {
    val nbt = this.getCompound(key) ?: return null

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
    nbt.putInt("z", value.z)

    this.put(key, nbt)
}
