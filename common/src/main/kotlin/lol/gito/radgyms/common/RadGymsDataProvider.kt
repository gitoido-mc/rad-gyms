/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common

import com.cobblemon.mod.common.Environment
import lol.gito.radgyms.common.api.data.DataProvider
import lol.gito.radgyms.common.api.data.DataRegistry
import lol.gito.radgyms.common.registry.RadGymsCaches
import lol.gito.radgyms.common.registry.RadGymsTemplates
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

object RadGymsDataProvider : DataProvider {
    private val registries = linkedSetOf<DataRegistry>()

    fun registerDefaults() {
        this.register(RadGymsTemplates)
        this.register(RadGymsCaches)

        if (RadGyms.implementation.environment() == Environment.CLIENT) {
            RadGyms.implementation.registerResourceReloader(
                RadGyms.modId("client_resources"),
                SimpleResourceReloader(PackType.CLIENT_RESOURCES),
                PackType.CLIENT_RESOURCES,
                emptyList()
            )
        }

        RadGyms.implementation.registerResourceReloader(
            RadGyms.modId("data_resources"),
            SimpleResourceReloader(PackType.SERVER_DATA),
            PackType.SERVER_DATA,
            emptyList()
        )
    }

    override fun <T : DataRegistry> register(registry: T): T {
        this.registries.add(registry)
        RadGyms.info("Registered the {} registry", registry.id.toString())
        RadGyms.debug(
            "Registered the {} registry of class {}",
            registry.id.toString(),
            registry::class.qualifiedName.toString()
        )

        return registry
    }

    override fun fromIdentifier(identifier: ResourceLocation): DataRegistry? =
        this.registries.find { it.id == identifier }

    override fun sync(player: ServerPlayer) {
        if (!player.connection.connection.isMemoryConnection) {
            this.registries.forEach { registry ->
                registry.sync(player)
            }
        }
    }

    private class SimpleResourceReloader(private val type: PackType): ResourceManagerReloadListener {
        override fun onResourceManagerReload(manager: ResourceManager) {
            registries
                .filter { it.type == this.type }
                .forEach { it.reload(manager) }
        }
    }
}
