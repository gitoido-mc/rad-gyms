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
import lol.gito.radgyms.common.command.CommandRegistry
import lol.gito.radgyms.common.config.RadGymsConfig
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.network.CommonNetworkStack
import lol.gito.radgyms.common.registry.*
import lol.gito.radgyms.common.util.displayClientMessage
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
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
    lateinit var implementation: RadGymsImplementation

    fun preInitialize(implementation: RadGymsImplementation) {

    }

    fun initialize() {
        LOGGER.info("Initializing the mod")
        loadConfig()

        // Data
        GymManager.register()
        GYM_LOADER.register()

        // Events
        EventManager.register()

        // TODO: neoforge block break
        PlayerBlockBreakEvents.BEFORE.register(::onBeforeBlockBreak)

        // Species
        SpeciesManager.register()

        // Registries
        RadGymsEntities.register { identifier, entry ->
            Registry.register(RadGymsEntities.registry, identifier, entry)
        }
        RadGymsEntities.registerAttributes { entityType, builder ->
            FabricDefaultAttributeRegistry.register(entityType, builder)
        }
        RadGymsDataComponents.register { identifier, entry ->
            Registry.register(RadGymsDataComponents.registry, identifier, entry)
        }
        RadGymsBlocks.register { identifier, entry ->
            Registry.register(RadGymsBlocks.registry, identifier, entry)
        }
        RadGymsBlockEntities.register { identifier, entry ->
            Registry.register(RadGymsBlockEntities.registry, identifier, entry)
        }
        RadGymsItems.register { identifier, item -> Registry.register(RadGymsItems.registry, identifier, item) }
        RadGymsItemGroups.register { provider ->
            Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB, provider.key, FabricItemGroup.builder()
                    .title(provider.displayName)
                    .icon(provider.displayIconProvider)
                    .displayItems(provider.entryCollector)
                    .build()
            )
        }

        CommandRegistry.register()

        // Network
        CommonNetworkStack.register()
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


    @Suppress("UNUSED_PARAMETER")
    fun onBeforeBlockBreak(
        world: Level,
        player: Player,
        pos: BlockPos,
        state: BlockState,
        entity: BlockEntity?
    ): Boolean {
        var allowBreak = true

        if (world.dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) {
            if (CONFIG.debug == true) return true

            allowBreak = false
        }

        if (state.block == RadGymsBlocks.GYM_ENTRANCE) {
            if (!player.isShiftKeyDown) {
                player.displayClientMessage(Component.translatable(modId("message.info.gym_entrance_breaking").toLanguageKey()))
                player.displayClientMessage(Component.translatable(modId("message.error.gym_entrance.not-sneaking").toLanguageKey()))
                allowBreak = false
            } else {
                allowBreak = true
            }
        }

        return allowBreak
    }
}