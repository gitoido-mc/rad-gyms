/*
 * Copyright (c) 2025-2026. gitoido-mc
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
import lol.gito.radgyms.common.api.RadGymsImplementation
import lol.gito.radgyms.common.api.data.DataProvider
import lol.gito.radgyms.common.command.argument.ElementalTypeArgumentType
import lol.gito.radgyms.common.command.argument.GymTemplateArgumentType
import lol.gito.radgyms.common.command.argument.RarityArgumentType
import lol.gito.radgyms.common.config.RadGymsConfig
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.gym.GymInitializer
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.stats.RadGymsStats
import net.minecraft.commands.synchronization.SingletonArgumentInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.border.WorldBorder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object RadGyms {
    const val MOD_ID: String = "rad_gyms"
    private const val CONFIG_PATH: String = "config/${MOD_ID}_server.json"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    @JvmField
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)

    @JvmField
    val dimensionWorldBorder: WorldBorder = WorldBorder()

    val statistics: RadGymsStats = RadGymsStats

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
        implementation.registerScoreboardObjectives()
        loadConfig()
        registerArgumentTypes()
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

    @JvmStatic
    fun modId(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    @JvmStatic
    fun info(message: String, vararg params: Any): Unit = LOGGER.info(message, *params)

    @JvmStatic
    fun warn(message: String, vararg params: Any): Unit = LOGGER.warn(message, *params)

    @JvmStatic
    fun debug(message: String, vararg params: Any) {
        if (CONFIG.debug == true) LOGGER.info(message, *params)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        if (configFile.exists()) {
            LOGGER.info("Loading config")
            CONFIG = configFile.inputStream().let {
                Json.decodeFromStream<RadGymsConfig>(it)
            }
        } else {
            LOGGER.info("No config found, creating new")
            CONFIG = RadGymsConfig.DEFAULT
            saveConfig(new = true)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveConfig(new: Boolean = false) {
        val prettify = Json {
            prettyPrint = true
        }
        val configFile = File(CONFIG_PATH)
        val config = when (new) {
            true -> RadGymsConfig.DEFAULT
            false -> CONFIG
        }

        configFile.outputStream().let {
            prettify.encodeToStream(config, it)
            debug("Saving config")
        }
    }

    private fun registerArgumentTypes() {
        this.implementation.registerCommandArgument(
            modId("rarity"),
            RarityArgumentType::class,
            SingletonArgumentInfo.contextFree(RarityArgumentType::rarity)
        )
        this.implementation.registerCommandArgument(
            modId("elemental_type"),
            ElementalTypeArgumentType::class,
            SingletonArgumentInfo.contextFree(ElementalTypeArgumentType::type)
        )
        this.implementation.registerCommandArgument(
            modId("template"),
            GymTemplateArgumentType::class,
            SingletonArgumentInfo.contextFree(GymTemplateArgumentType::templates)
        )
    }
}
