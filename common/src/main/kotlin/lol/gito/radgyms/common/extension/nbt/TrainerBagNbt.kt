/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.nbt

import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsTrainerBag(key: String): MutableList<BagItemModel> {
    val nbt = this.getCompound(key)

    return nbt.allKeys.map {
        BagItemModel(it, nbt.getInt(it))
    }.toMutableList()
}

fun CompoundTag.putRadGymsTrainerBag(key: String, value: MutableList<BagItemModel>) {
    val nbt = CompoundTag()

    value.forEach {
        nbt.putInt(it.item, it.quantity)
    }

    this.put(key, nbt)
}
