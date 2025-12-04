/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common

import com.gitlab.srcmc.rctapi.api.RCTApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import lol.gito.radgyms.common.command.CommandRegistry
import lol.gito.radgyms.common.config.RadGymsConfig
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.fabric.RadGymsDataLoader
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object RadGyms {
    const val MOD_ID: String = "rad-gyms"
    private const val CONFIG_PATH: String = "config/${MOD_ID}_server.json"
    private val GYM_LOADER: RadGymsDataLoader = RadGymsDataLoader()
    val gymTemplateRegistry: RadGymsTemplates = RadGymsTemplates

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    @JvmField
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)
    val gymTemplateRegistry: RadGymsTemplates = RadGymsTemplates

    lateinit var CONFIG: RadGymsConfig
    lateinit var implementation: RadGymsImplementation

    fun preInitialize(implementation: RadGymsImplementation) {
        this.implementation = implementation
        implementation.registerDataComponents()
        implementation.registerBlocks()
        implementation.registerItems()
        implementation.registerEntityTypes()
        implementation.registerEntityAttributes()
        implementation.registerBlockEntityTypes()
        loadConfig()
    }

    fun initialize() {
        LOGGER.info("Initializing the mod")
        // Data
        GYM_LOADER.register()
        // Events
        EventManager.register()
        // Species
        SpeciesManager.register()
        // Registries
        CommandRegistry.register()
    }

    fun modId(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    @Suppress("unused")
    fun log(message: String): Unit = LOGGER.info(message)

    @Suppress("unused")
    fun warn(message: String): Unit = LOGGER.warn(message)

    @Suppress("unused")
    fun debug(message: String) {
        if (CONFIG.debug == true) LOGGER.info(message)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        CONFIG = RadGymsConfig(
            debug = false,
            // Should average team level be derived automatically
            deriveAverageGymLevel = true,
            // Gym level bounds
            minLevel = 10,
            maxLevel = 100,
            // Gym entrance max uses per player
            maxEntranceUses = 3,
            // Cache shiny boost amount per unit of lapis
            lapisBoostAmount = 1,
            // Add shard rewards
            shardRewards = true,
            // Ignored species
            ignoredSpecies = emptyList(),
            // Ignored forms - by default ignore all battle forms
            ignoredForms = mutableListOf("gmax", "mega", "mega-x", "mega-y", "stellar", "terastal"),
        )

        if (configFile.exists()) {
            LOGGER.info("Loading config")
            val fileConfig = configFile.inputStream().let {
                Json.decodeFromStream<RadGymsConfig>(it)
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