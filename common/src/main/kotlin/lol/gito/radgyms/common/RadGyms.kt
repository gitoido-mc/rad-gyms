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
import lol.gito.radgyms.common.api.data.DataProvider
import lol.gito.radgyms.common.config.RadGymsConfig
import lol.gito.radgyms.common.data.RadGymsDataProvider
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.gym.GymInitializer
import lol.gito.radgyms.common.gym.SpeciesManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.border.WorldBorder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object RadGyms {
    const val MOD_ID: String = "rad-gyms"

    private const val CONFIG_PATH: String = "config/${MOD_ID}_server.json"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    @JvmField
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)
    @JvmField

    val dimensionWorldBorder: WorldBorder = WorldBorder()
    val dataProvider: DataProvider = RadGymsDataProvider


    lateinit var CONFIG: RadGymsConfig
    lateinit var implementation: RadGymsImplementation
    lateinit var gymInitializer: GymInitializer

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
        // Species
        SpeciesManager.register()
        // Data
        RadGymsDataProvider.registerDefaults()
        // Events
        EventManager.register()
    }

    fun modId(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    fun info(message: String, vararg params: Any?): Unit = LOGGER.info(message, params)

    @Suppress("unused")
    fun warn(message: String, vararg params: Any?): Unit = LOGGER.warn(message, params)

    @Suppress("unused")
    fun debug(message: String, vararg params: Any?) {
        if (CONFIG.debug == true) LOGGER.info(message, params)
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