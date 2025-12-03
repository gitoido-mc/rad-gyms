/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import com.cobblemon.mod.fabric.CobblemonFabric
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.RadGymsDataLoader
import lol.gito.radgyms.common.RadGymsImplementation
import lol.gito.radgyms.common.registry.*
import lol.gito.radgyms.common.util.displayClientMessage
import lol.gito.radgyms.fabric.net.RadGymsFabricNetworkManager
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

object RadGymsFabric : RadGymsImplementation {
    override val modAPI: ModAPI = ModAPI.FABRIC

    private var server: MinecraftServer? = null

    override val networkManager = RadGymsFabricNetworkManager

    override fun environment(): Environment = CobblemonFabric.environment()

    override fun isModInstalled(id: String): Boolean = CobblemonFabric.isModInstalled(id)


    override fun initialize() {
        RadGyms.preInitialize(this)
        RadGyms.initialize()
        networkManager.registerMessages()
        networkManager.registerServerHandlers()
        PlayerBlockBreakEvents.BEFORE.register(::onBeforeBlockBreak)
    }

    override fun registerDataComponents() = RadGymsDataComponents.register { identifier, component ->
        Registry.register(RadGymsDataComponents.registry, identifier, component)
    }

    override fun registerItems() {
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
    }

    override fun registerBlocks() = RadGymsBlocks.register { identifier, entry ->
        Registry.register(RadGymsBlocks.registry, identifier, entry)
    }

    override fun registerEntityTypes() = RadGymsEntities.register { identifier, entry ->
        Registry.register(RadGymsEntities.registry, identifier, entry)
    }

    override fun registerEntityAttributes() = RadGymsEntities.registerAttributes { entityType, builder ->
        FabricDefaultAttributeRegistry.register(entityType, builder)
    }

    override fun registerBlockEntityTypes() {
        RadGymsBlockEntities.register { identifier, entry ->
            Registry.register(RadGymsBlockEntities.registry, identifier, entry)
        }
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    ) {
        ResourceManagerHelper.get(type).registerReloadListener(RadGymsDataLoader)
    }

    override fun server(): MinecraftServer? = when (this.environment()) {
        Environment.CLIENT -> Minecraft.getInstance().singleplayerServer
        Environment.SERVER -> this.server
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