/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import com.cobblemon.mod.common.NetworkManager
import com.cobblemon.mod.fabric.CobblemonFabric
import com.cobblemon.mod.fabric.net.CobblemonFabricNetworkManager
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGymsImplementation
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

object RadGymsFabric : RadGymsImplementation {
    override val modAPI: ModAPI = ModAPI.FABRIC

    private var server: MinecraftServer? = null

    override val networkManager: NetworkManager = CobblemonFabricNetworkManager

    override fun initialize() {
        RadGyms.initialize()
    }

    override fun environment(): Environment = CobblemonFabric.environment()

    override fun isModInstalled(id: String): Boolean = CobblemonFabric.isModInstalled(id)

    override fun registerDataComponents() = RadGymsDataComponents.register { identifier, component ->
        Registry.register(RadGymsDataComponents.registry, identifier, component)
    }

    override fun registerItems() {

    }

    override fun registerBlocks() {
        TODO("Not yet implemented")
    }

    override fun registerEntityTypes() {
        TODO("Not yet implemented")
    }

    override fun registerBlockEntityTypes() {
        TODO("Not yet implemented")
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    ) {
        TODO("Not yet implemented")
    }

    override fun server(): MinecraftServer? {
        TODO("Not yet implemented")
    }
}