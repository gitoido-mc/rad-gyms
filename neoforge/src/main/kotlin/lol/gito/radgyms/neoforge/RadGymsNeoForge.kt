/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.neoforge

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import com.mojang.brigadier.arguments.ArgumentType
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.MOD_ID
import lol.gito.radgyms.common.RadGyms.config
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.api.RadGymsImplementation
import lol.gito.radgyms.common.command.RadGymsCommands
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsBlockEntities
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsDimensions.GYM_DIMENSION
import lol.gito.radgyms.common.registry.RadGymsEntities
import lol.gito.radgyms.common.registry.RadGymsItemGroups
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.neoforge.client.RadGymsNeoForgeClient
import lol.gito.radgyms.neoforge.net.RadGymsNeoForgeNetworkManager
import net.minecraft.commands.synchronization.ArgumentTypeInfo
import net.minecraft.commands.synchronization.ArgumentTypeInfos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.stats.Stats
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
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.*
import kotlin.reflect.KClass

@Mod(MOD_ID)
class RadGymsNeoForge : RadGymsImplementation {

    override val modAPI: ModAPI = ModAPI.NEOFORGE
    private val hasBeenSynced = hashSetOf<UUID>()
    private val commandArgumentTypes = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, MOD_ID)
    private val reloadableResources = arrayListOf<PreparableReloadListener>()
    private val queuedWork = arrayListOf<() -> Unit>()
    override val networkManager = RadGymsNeoForgeNetworkManager

    init {
        with(MOD_BUS) {
            this@RadGymsNeoForge.commandArgumentTypes.register(this)
            addListener(this@RadGymsNeoForge::initialize)
            RadGyms.preInitialize(this@RadGymsNeoForge)
            addListener(this@RadGymsNeoForge::on)
            addListener(EventPriority.HIGH, ::onBuildContents)
            addListener(networkManager::registerMessages)
        }
        with(NeoForge.EVENT_BUS) {
            addListener(::onDataPackSync)
            addListener(::onLogin)
            addListener(::onLogout)
            addListener(::onReload)
            addListener(::onBlockBreak)
            addListener(::registerCommands)
        }

        if (FMLEnvironment.dist == Dist.CLIENT) {
            RadGymsNeoForgeClient.init()
        }
    }

    override fun isModInstalled(id: String) = ModList.get().isLoaded(id)
    override fun environment(): Environment =
        if (FMLEnvironment.dist.isClient) Environment.CLIENT else Environment.SERVER

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
                        val itemGroup =
                            CreativeModeTab
                                .builder()
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

    override fun registerBlockEntityTypes() {
        MOD_BUS.addListener<RegisterEvent> { event ->
            event.register(RadGymsBlockEntities.resourceKey) { helper ->
                RadGymsBlockEntities.register { identifier, block ->
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

    fun on(event: RegisterEvent) {
        event.register(Registries.CUSTOM_STAT) { registry ->
            RadGyms.statistics.registerStats()
            RadGyms.statistics.store.values.forEach {
                registry.register(it.resourceLocation, it.resourceLocation)
                Stats.CUSTOM.get(it.resourceLocation, it.formatter)
            }
        }
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>,
    ) {
        if (type == PackType.SERVER_DATA) {
            this.reloadableResources += reloader
        } else {
            RadGymsNeoForgeClient.registerResourceReloader(reloader)
        }
    }

    override fun <A : ArgumentType<*>, T : ArgumentTypeInfo.Template<A>> registerCommandArgument(
        identifier: ResourceLocation,
        argumentClass: KClass<A>,
        serializer: ArgumentTypeInfo<A, T>,
    ) {
        this.commandArgumentTypes.register(identifier.path) { _ ->
            ArgumentTypeInfos.registerByClass(argumentClass.java, serializer)
        }
    }

    override fun server(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()
    override fun initialize() = Unit
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

    private fun registerCommands(e: RegisterCommandsEvent) {
        RadGymsCommands.register(e.dispatcher, e.buildContext, e.commandSelection)
    }

    private fun onReload(e: AddReloadListenerEvent) {
        this.reloadableResources.forEach(e::addListener)
    }

    private fun onBuildContents(e: BuildCreativeModeTabContentsEvent) {
        val forgeInject = ForgeItemGroupInject(e)
        RadGymsItemGroups.inject(e.tabKey, forgeInject)
    }

    private fun onBlockBreak(e: BlockEvent.BreakEvent) {
        var canCancel: Boolean
        canCancel = (e.level !is ServerLevel)
        if (!canCancel && (e.level as ServerLevel).dimension() == GYM_DIMENSION) {
            canCancel = (config.debug == true)
            if (!canCancel) {
                e.isCanceled = true
            }
        }

        if (canCancel) return

        if (e.state.block == RadGymsBlocks.GYM_ENTRANCE) {
            if (!e.player.isShiftKeyDown) {
                e.player.displayClientMessage(tl("message.info.gym_entrance_breaking"))
                e.player.displayClientMessage(tl("message.error.gym_entrance.not-sneaking"))
                e.isCanceled = false
            } else {
                e.isCanceled = true
            }
        }
    }

    private class ForgeItemGroupInject(@Suppress("unused") private val entries: BuildCreativeModeTabContentsEvent) :
        RadGymsItemGroups.Injector {
        override fun putFirst(item: ItemLike) {
            this.entries.insertFirst(ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
        }

        override fun putBefore(item: ItemLike, target: ItemLike) = this.entries.insertBefore(
            ItemStack(target),
            ItemStack(item),
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
        )

        override fun putAfter(item: ItemLike, target: ItemLike) = this.entries.insertAfter(
            ItemStack(target),
            ItemStack(item),
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
        )

        override fun putLast(item: ItemLike) =
            this.entries.accept(ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)

        override fun putLast(item: ItemStack) =
            this.entries.accept(item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
    }
}
