/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.util.server
import lol.gito.radgyms.common.api.data.DataProvider
import lol.gito.radgyms.common.api.data.DataRegistry
import lol.gito.radgyms.common.api.data.ServerJsonDataRegistry
import lol.gito.radgyms.common.registry.RadGymsCaches
import lol.gito.radgyms.common.registry.RadGymsTemplates
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

object RadGymsDataProvider : DataProvider {
    private val registries = linkedSetOf<DataRegistry>()
    private val reloadableRegistries = linkedSetOf<DataRegistry>()

    fun registerDefaults() {
        this.register(RadGymsTemplates)
        this.register(RadGymsCaches)

        if (RadGyms.implementation.environment() == Environment.CLIENT) {
            RadGyms.implementation.registerResourceReloader(
                RadGyms.modId("client_resources"),
                PackType.CLIENT_RESOURCES,
                emptyList(),
            ) { ClientResourceReloader() }
        }

        RadGyms.implementation.registerResourceReloader(
            RadGyms.modId("data_resources"),
            PackType.SERVER_DATA,
            emptyList(),
        ) { ServerResourceReloader(it) }
    }

    override fun <T : DataRegistry> register(registry: T): T {
        this.registries.add(registry)
        RadGyms.info("Registered the {} registry", registry.id.toString())
        RadGyms.debug(
            "Registered the {} registry of class {}",
            registry.id.toString(),
            registry::class.qualifiedName.toString(),
        )

        return registry
    }

    override fun fromIdentifier(identifier: ResourceLocation): DataRegistry? = this.registries.find { it.id == identifier }

    override fun sync(player: ServerPlayer) {
        if (!player.connection.connection.isMemoryConnection) {
            this.registries.forEach { registry ->
                registry.sync(player)
            }
        }
    }

    private class ClientResourceReloader : ResourceManagerReloadListener {
        override fun onResourceManagerReload(manager: ResourceManager) {
            registries.filter { it.type == PackType.CLIENT_RESOURCES }.forEach { it.reload(manager) }
        }
    }

    private class ServerResourceReloader(private val registryAccess: HolderLookup.Provider) : ResourceManagerReloadListener {
        override fun onResourceManagerReload(manager: ResourceManager) {
            // Check for a server running, this is due to the create a world screen triggering datapack reloads, these are fine to happen as many times as needed as players may be in the process of adding their datapacks.
            val reloadAllowed = server()?.isReady != true
            registries
                .filter { it.type == PackType.SERVER_DATA && (reloadAllowed || it in reloadableRegistries) }
                .filterIsInstance<ServerJsonDataRegistry<*>>()
                .forEach { it.reload(manager, registryAccess) }
            registries
                .filter { it.type == PackType.SERVER_DATA && (reloadAllowed || it in reloadableRegistries) }
                .forEach { it.reload(manager) }
        }
    }
}
