/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.gitlab.srcmc.rctapi.api.RCTApi
import lol.gito.radgyms.common.api.RadGymsImplementation
import lol.gito.radgyms.common.api.data.DataProvider
import lol.gito.radgyms.common.command.argument.ElementalTypeArgumentType
import lol.gito.radgyms.common.command.argument.GymTemplateArgumentType
import lol.gito.radgyms.common.command.argument.RarityArgumentType
import lol.gito.radgyms.common.config.RadGymsConfigs
import lol.gito.radgyms.common.event.EventManager
import lol.gito.radgyms.common.extension.cobblemon.molang.NPCEntityRGBridge
import lol.gito.radgyms.common.gym.GymInitializer
import lol.gito.radgyms.common.registry.RadGymsSpeciesRegistry
import lol.gito.radgyms.common.registry.RadGymsStats
import net.minecraft.commands.synchronization.SingletonArgumentInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.border.WorldBorder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RadGyms {
    const val MOD_ID: String = "rad_gyms"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    @JvmField
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)

    @JvmField
    val dimensionWorldBorder: WorldBorder = WorldBorder()

    val statistics: RadGymsStats = RadGymsStats

    val dataProvider: DataProvider = RadGymsDataProvider

    lateinit var implementation: RadGymsImplementation
    lateinit var gymInitializer: GymInitializer

    val defaultElementalTypes = setOf(
        ElementalTypes.BUG.showdownId,
        ElementalTypes.DARK.showdownId,
        ElementalTypes.DRAGON.showdownId,
        ElementalTypes.ELECTRIC.showdownId,
        ElementalTypes.FAIRY.showdownId,
        ElementalTypes.FLYING.showdownId,
        ElementalTypes.FIGHTING.showdownId,
        ElementalTypes.FIRE.showdownId,
        ElementalTypes.FLYING.showdownId,
        ElementalTypes.GHOST.showdownId,
        ElementalTypes.GRASS.showdownId,
        ElementalTypes.GROUND.showdownId,
        ElementalTypes.ICE.showdownId,
        ElementalTypes.NORMAL.showdownId,
        ElementalTypes.POISON.showdownId,
        ElementalTypes.PSYCHIC.showdownId,
        ElementalTypes.ROCK.showdownId,
        ElementalTypes.STEEL.showdownId,
        ElementalTypes.WATER.showdownId,
    )

    fun preInitialize(implementation: RadGymsImplementation) {
        this.implementation = implementation
        RadGymsConfigs.init(implementation.configDir())
        RadGymsConfigs.load()
        implementation.registerDataComponents()
        implementation.registerBlocks()
        implementation.registerItems()
        implementation.registerEntityTypes()
        implementation.registerEntityAttributes()
        implementation.registerBlockEntityTypes()
        registerArgumentTypes()
    }

    fun initialize() {
        LOGGER.info("Initializing the mod")
        // Species
        RadGymsSpeciesRegistry.register()
        // Data
        RadGymsDataProvider.registerDefaults()
        // Events
        EventManager.register()
        // Molang
        NPCEntityRGBridge.init()
    }

    @JvmStatic
    fun modId(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    @JvmStatic
    fun info(message: String, vararg params: Any): Unit = LOGGER.info(message, *params)

    @JvmStatic
    fun warn(message: String, vararg params: Any): Unit = LOGGER.warn(message, *params)

    @JvmStatic
    fun debug(message: String, vararg params: Any) {
        if (RadGymsConfigs.server.debug) LOGGER.info(message, *params)
    }

    private fun registerArgumentTypes() {
        this.implementation.registerCommandArgument(
            modId("rarity"),
            RarityArgumentType::class,
            SingletonArgumentInfo.contextFree(RarityArgumentType::rarity),
        )
        this.implementation.registerCommandArgument(
            modId("elemental_type"),
            ElementalTypeArgumentType::class,
            SingletonArgumentInfo.contextFree(ElementalTypeArgumentType::type),
        )
        this.implementation.registerCommandArgument(
            modId("template"),
            GymTemplateArgumentType::class,
            SingletonArgumentInfo.contextFree(GymTemplateArgumentType::templates),
        )
    }
}
