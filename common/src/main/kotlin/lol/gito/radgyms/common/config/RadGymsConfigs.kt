/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.config

import com.google.gson.GsonBuilder
import lol.gito.radgyms.common.CONFIG_050
import lol.gito.radgyms.common.RadGyms.MOD_ID
import java.nio.file.Files
import java.nio.file.Path

object RadGymsConfigs {
    private val gson = GsonBuilder().setPrettyPrinting().setVersion(CONFIG_050).create()

    private lateinit var configDir: Path

    lateinit var server: ServerConfig
    lateinit var client: ClientConfig

    fun init(configDir: Path) {
        RadGymsConfigs.configDir = configDir
    }

    @Throws(IllegalStateException::class)
    fun load() {
        check(this::configDir.isInitialized)

        server = loadJson(
            configDir.resolve("${MOD_ID}_server.json"),
            ServerConfig::class.java,
            ServerConfig(),
        )
    }

    fun loadClient() {
        check(this::configDir.isInitialized)

        client = loadJson(
            configDir.resolve("${MOD_ID}_client.json"),
            ClientConfig::class.java,
            ClientConfig(),
        )
    }

    private fun <T> loadJson(path: Path, type: Class<T>, defaults: T): T = try {
        Files.createDirectories(path.parent)

        if (!Files.exists(path)) {
            Files.newBufferedWriter(path).use { w ->
                gson.toJson(defaults, w)
            }
            return defaults
        }

        Files.newBufferedReader(path).use { r ->
            val parsed: T? = gson.fromJson(r, type)
            return parsed ?: defaults
        }
    } catch (_: Exception) {
        return defaults
    }

    fun reload() = load()

    fun reloadClient() {
        loadClient()
    }

    fun sync(config: ServerConfig) {
        server = config
    }
}
