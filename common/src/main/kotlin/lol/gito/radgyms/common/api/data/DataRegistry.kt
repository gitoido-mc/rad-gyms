/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.data

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager

interface DataRegistry {
    val id: ResourceLocation

    val type: PackType

    val observable: SimpleObservable<out DataRegistry>

    fun reload(manager: ResourceManager)

    fun sync(player: ServerPlayer)
}