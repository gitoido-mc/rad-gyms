/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import com.cobblemon.mod.common.NetworkManager
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.commands.synchronization.ArgumentTypeInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import kotlin.reflect.KClass

interface RadGymsImplementation {
    /**
     * Returns enum corresponding to mod loader being used.
     * For example fabric mod loader will return ModAPI.FABRIC
     */
    val modAPI: ModAPI

    val networkManager: NetworkManager

    fun environment(): Environment

    fun isModInstalled(id: String): Boolean

    fun registerDataComponents()

    fun registerItems()

    fun registerBlocks()

    fun registerEntityTypes()

    fun registerEntityAttributes()

    fun registerBlockEntityTypes()

    fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>,
    )

    fun <A : ArgumentType<*>, T : ArgumentTypeInfo.Template<A>> registerCommandArgument(
        identifier: ResourceLocation,
        argumentClass: KClass<A>,
        serializer: ArgumentTypeInfo<A, T>,
    )

    fun server(): MinecraftServer?

    fun initialize()
}
