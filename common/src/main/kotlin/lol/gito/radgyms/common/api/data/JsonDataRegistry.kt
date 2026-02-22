/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.data

import com.cobblemon.mod.common.util.endsWith
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutionException

interface JsonDataRegistry<T> : DataRegistry {
    val resourcePath: String

    companion object {
        const val JSON_EXTENSION = ".json"
    }

    @Throws(ExecutionException::class)
    override fun reload(manager: ResourceManager) {
        val data = hashMapOf<ResourceLocation, T>()

        manager
            .listResources(resourcePath) { it.endsWith(JSON_EXTENSION) }
            .forEach { (identifier, resource) ->
                try {
                    resource.open().let {
                        val derivedIdentifier =
                            ResourceLocation.fromNamespaceAndPath(
                                identifier.namespace,
                                File(identifier.path).nameWithoutExtension,
                            )

                        data[derivedIdentifier] = parse(it, derivedIdentifier)
                    }
                } catch (exception: IOException) {
                    throw ExecutionException("Error loading resource $identifier", exception)
                }
            }

        reload(data)
    }

    fun parse(stream: InputStream, identifier: ResourceLocation): T

    fun reload(data: Map<ResourceLocation, T>)
}
