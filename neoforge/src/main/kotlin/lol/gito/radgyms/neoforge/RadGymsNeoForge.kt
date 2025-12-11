/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.neoforge

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.RadGymsImplementation
import lol.gito.radgyms.common.registry.*
import lol.gito.radgyms.neoforge.client.RadGymsNeoForgeClient
import lol.gito.radgyms.neoforge.net.RadGymsNeoForgeNetworkManager
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.EventPriority
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.OnDatapackSyncEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.*

@Mod(RadGyms.MOD_ID)
class RadGymsNeoForge : RadGymsImplementation {
    override val modAPI: ModAPI = ModAPI.NEOFORGE

    private val hasBeenSynced = hashSetOf<UUID>()
    private val reloadableResources = arrayListOf<PreparableReloadListener>()
    private val queuedWork = arrayListOf<() -> Unit>()
    override val networkManager = RadGymsNeoForgeNetworkManager

    init {
        with(MOD_BUS) {
            addListener(this@RadGymsNeoForge::initialize)
            RadGyms.preInitialize(this@RadGymsNeoForge)
            addListener(EventPriority.HIGH, ::onBuildContents)
            addListener(networkManager::registerMessages)
        }
        with(NeoForge.EVENT_BUS) {
            addListener(::onDataPackSync)
            addListener(::onLogin)
            addListener(::onLogout)
            addListener(::onReload)
        }

        if (FMLEnvironment.dist == Dist.CLIENT) {
            RadGymsNeoForgeClient.init()
        }
    }

    override fun isModInstalled(id: String) = ModList.get().isLoaded(id)

    override fun environment(): Environment {
        return if (FMLEnvironment.dist.isClient) Environment.CLIENT else Environment.SERVER
    }

    override fun registerDataComponents() {
        MOD_BUS.addListener<RegisterEvent> { event ->
            event.register(RadGymsDataComponents.resourceKey) { helper ->
                RadGymsDataComponents.register { identifier, componentType ->
                    helper.register(identifier, componentType)
                }
            }
        }
    }

    override fun registerItems() {
        with(MOD_BUS) {
            addListener<RegisterEvent> { event ->
                event.register(RadGymsItems.resourceKey) { helper ->
                    RadGymsItems.register { identifier, item ->
                        helper.register(identifier, item)
                    }
                }
            }
            addListener<RegisterEvent> { event ->
                event.register(Registries.CREATIVE_MODE_TAB) { helper ->
                    RadGymsItemGroups.register { holder ->
                        val itemGroup = CreativeModeTab.builder()
                            .title(holder.displayName)
                            .icon(holder.displayIconProvider)
                            .displayItems(holder.entryCollector)
                            .build()
                        helper.register(holder.key, itemGroup)
                        itemGroup
                    }
                }
            }
        }
    }

    override fun registerBlocks() {
        MOD_BUS.addListener<RegisterEvent> { event ->
            event.register(RadGymsBlocks.resourceKey) { helper ->
                RadGymsBlocks.register { identifier, block ->
                    helper.register(identifier, block)
                }
            }
        }
    }

    override fun registerEntityTypes() {
        MOD_BUS.addListener<RegisterEvent> { event ->
            event.register(RadGymsEntities.resourceKey) { helper ->
                RadGymsEntities.register { identifier, block ->
                    helper.register(identifier, block)
                }
            }
        }
    }

    override fun registerEntityAttributes() {
        MOD_BUS.addListener<EntityAttributeCreationEvent> { event ->
            RadGymsEntities.registerAttributes { type, builder ->
                event.put(type, builder.build())
            }
        }
    }

    override fun registerBlockEntityTypes() {
        MOD_BUS.addListener<RegisterEvent> { event ->
            event.register(RadGymsBlockEntities.resourceKey) { helper ->
                RadGymsBlockEntities.register { identifier, block ->
                    helper.register(identifier, block)
                }
            }
        }
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    ) {
        if (type == PackType.SERVER_DATA) {
            this.reloadableResources += reloader
        } else {
            RadGymsNeoForgeClient.registerResourceReloader(reloader)
        }
    }

    private fun onReload(e: AddReloadListenerEvent) {
        this.reloadableResources.forEach(e::addListener)
    }

    override fun server(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()


    override fun initialize() {}

    fun initialize(event: FMLCommonSetupEvent) {
        info("Initializing Rad Gyms for NeoForge!")
        event.enqueueWork {
            this.queuedWork.forEach { it.invoke() }
        }
        RadGyms.initialize()
    }

    fun onDataPackSync(event: OnDatapackSyncEvent) {
        RadGyms.dataProvider.sync(event.player ?: return)
    }

    fun onLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        this.hasBeenSynced.add(event.entity.uuid)
    }

    fun onLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
        this.hasBeenSynced.remove(event.entity.uuid)
    }

    private fun onBuildContents(e: BuildCreativeModeTabContentsEvent) {
        val forgeInject = ForgeItemGroupInject(e)
        RadGymsItemGroups.inject(e.tabKey, forgeInject)
    }


    private class ForgeItemGroupInject(private val entries: BuildCreativeModeTabContentsEvent) :
        RadGymsItemGroups.Injector {

        override fun putFirst(item: ItemLike) {
            this.entries.insertFirst(ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
        }

        override fun putBefore(item: ItemLike, target: ItemLike) {
            this.entries.insertBefore(
                ItemStack(target),
                ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            )
        }

        override fun putAfter(item: ItemLike, target: ItemLike) {
            this.entries.insertAfter(
                ItemStack(target),
                ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            )
        }

        override fun putLast(item: ItemLike) {
            this.entries.accept(ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
        }
    }

}