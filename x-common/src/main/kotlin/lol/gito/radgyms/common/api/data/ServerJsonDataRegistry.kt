/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.data

import com.cobblemon.mod.common.util.endsWith
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import java.io.BufferedReader
import java.io.File
import java.util.concurrent.ExecutionException

/**
 * A [DataRegistry] that consumes JSON files.
 * Every deserialized instance is attached to an [ResourceLocation].
 * For example a file under data/mymod/[resourcePath]/entry.json would be backed by the identifier modid:entry.
 *
 * @param T The type of the data consumed by this registry.
 *
 * @author Licious
 * @since August 5th, 2022
 */
interface ServerJsonDataRegistry<T> : DataRegistry {
    /**
     * The [TypeToken] of type [T].
     */
    val typeToken: TypeToken<T>

    /**
     * The folder location for the data this registry will consume.
     */
    val resourcePath: String

    override val type: PackType
        get() = PackType.SERVER_DATA

    override fun reload(manager: ResourceManager) {
        // NO-OP, handled by reload(manager, registry)
    }

    fun reload(manager: ResourceManager, registry: HolderLookup.Provider) {
        val gson = setupParser(registry)
        val data = hashMapOf<ResourceLocation, T>()
        manager.listResources(resourcePath) { path -> path.endsWith(JSON_EXTENSION) }
            .forEach { (identifier, resource) ->
                if (identifier.namespace == "pixelmon") {
                    return@forEach
                }

                resource.open().use { stream ->
                    stream.bufferedReader().use { reader ->
                        val resolvedIdentifier = ResourceLocation.fromNamespaceAndPath(
                            identifier.namespace,
                            File(identifier.path).nameWithoutExtension,
                        )
                        data[resolvedIdentifier] = parse(reader, resolvedIdentifier, gson)
                    }
                }
            }

        reload(data, registry)
    }

    fun parse(reader: BufferedReader, identifier: ResourceLocation, gson: Gson): T = try {
        gson.fromJson(reader, typeToken.type)
    } catch (exception: Exception) {
        throw ExecutionException("Error loading JSON for data: $identifier", exception)
    }

    /**
     * Allows the creation of [Gson] instances which have adapters that are registry-aware, e.g. [com.cobblemon.mod.common.pokemon.evolution.adapters.LegacyItemConditionWrapperAdapter]
     */
    fun setupParser(registry: HolderLookup.Provider): Gson

    /**
     * Reloads this registry from the deserialized data.
     *
     * @param data A map of the data associating an instance to the respective identifier from the [ResourceManager].
     */
    fun reload(data: Map<ResourceLocation, T>, registry: HolderLookup.Provider)

    companion object {
        const val JSON_EXTENSION = ".json"
    }
}
