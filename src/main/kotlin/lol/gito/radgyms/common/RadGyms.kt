/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common

import com.gitlab.srcmc.rctapi.api.RCTApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.network.CommonNetworkStack
import lol.gito.radgyms.common.registry.*
import lol.gito.radgyms.server.command.CommandRegistry
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object RadGyms {
    const val MOD_ID: String = "rad-gyms"
    private const val CONFIG_PATH: String = "config/${MOD_ID}_server.json"
    private val GYM_LOADER: RadGymsDataLoader = RadGymsDataLoader()
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)

    lateinit var CONFIG: RadGymsConfig

    fun init() {
        LOGGER.info("Initializing the mod")
        loadConfig()
        // Data
        EntityRegistry.register()
        GymManager.register()
        GYM_LOADER.register()

        // Events
        EventManager.register()

        // Species
        SpeciesManager.register()

        // Registries
        DimensionRegistry.register()
        DataComponentRegistry.register()
        BlockRegistry.register()
        BlockEntityRegistry.register()
        ItemRegistry.register()
        ItemGroupRegistry.register()
        CommandRegistry.register()

        // Network
        CommonNetworkStack.register()

    }

    fun modId(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }

    fun debug(message: String) {
        if (CONFIG.debug == true) LOGGER.info(message)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        CONFIG = RadGymsConfig(
            debug = false,
            maxEntranceUses = 3,
            lapisBoostAmount = 1,
            shardRewards = true,
            ignoredSpecies = emptyList(),
            ignoredForms = mutableListOf("gmax"),
        )

        if (configFile.exists()) {
            LOGGER.info("Loading config")
            val fileConfig = configFile.inputStream().let {
                Json.Default.decodeFromStream<RadGymsConfig>(it)
            }

            CONFIG = CONFIG.combine(fileConfig)
        } else {
            LOGGER.info("No config found, creating new")
        }
        saveConfig()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveConfig() {
        val configFile = File(CONFIG_PATH)
        val prettify = Json {
            prettyPrint = true
        }
        configFile.outputStream().let {
            prettify.encodeToStream(CONFIG, it)
            debug("Saving config")
        }
    }
}