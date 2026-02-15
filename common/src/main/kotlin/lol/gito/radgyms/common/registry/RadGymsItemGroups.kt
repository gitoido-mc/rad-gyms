/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike

object RadGymsItemGroups {
    private val ALL = arrayListOf<ItemGroupHolder>()
    private val INJECTORS = hashMapOf<ResourceKey<CreativeModeTab>, (injector: Injector) -> Unit>()

    @JvmStatic
    val GENERAL_GROUP_KEY = this.create(modId("general"), this::generalEntries) {
        ItemStack(RadGymsItems.GYM_ENTRANCE)
    }

    @JvmStatic
    val KEYS_GROUP_KEY = this.create(modId("keys"), this::keyEntries) {
        ItemStack(RadGymsItems.GYM_KEY)
    }

    @JvmStatic
    val CACHES_GROUP_KEY = this.create(modId("caches"), this::cacheEntries) {
        ItemStack(RadGymsItems.CACHE_EPIC)
    }

    @JvmStatic
    val GENERAL_GROUP get() = BuiltInRegistries.CREATIVE_MODE_TAB.get(GENERAL_GROUP_KEY)

    @JvmStatic
    val KEYS_GROUP get() = BuiltInRegistries.CREATIVE_MODE_TAB.get(KEYS_GROUP_KEY)

    @JvmStatic
    val CACHES_GROUP get() = BuiltInRegistries.CREATIVE_MODE_TAB.get(CACHES_GROUP_KEY)

    fun register(consumer: (holder: ItemGroupHolder) -> CreativeModeTab) {
        ALL.forEach(consumer::invoke)
    }

    private fun generalEntries(
        @Suppress("unused") displayContext: CreativeModeTab.ItemDisplayParameters,
        entries: CreativeModeTab.Output
    ) {
        entries.accept(RadGymsItems.EXIT_ROPE)
        entries.accept(RadGymsItems.SHARD_COMMON)
        entries.accept(RadGymsItems.SHARD_UNCOMMON)
        entries.accept(RadGymsItems.SHARD_RARE)
        entries.accept(RadGymsItems.SHARD_EPIC)
        entries.accept(RadGymsItems.GYM_ENTRANCE)
        entries.accept(RadGymsItems.GYM_EXIT)
        entries.accept(RadGymsItems.SHARD_BLOCK_COMMON)
        entries.accept(RadGymsItems.SHARD_BLOCK_UNCOMMON)
        entries.accept(RadGymsItems.SHARD_BLOCK_EPIC)
        entries.accept(RadGymsItems.SHARD_BLOCK_RARE)
    }

    private fun keyEntries(
        @Suppress("unused") displayContext: CreativeModeTab.ItemDisplayParameters,
        entries: CreativeModeTab.Output
    ) {
        entries.accept(RadGymsItems.GYM_KEY)
        ElementalTypes.all().forEach { type ->
            val stack = RadGymsItems.GYM_KEY.defaultInstance.also {
                it.set(DataComponents.RARITY, Rarity.RARE)
                it.set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)
            }
            entries.accept(stack)
        }

        RadGymsTemplates
            .templates
            .filterNot { it.key == "default" }
            .filterNot { template -> template.key in ElementalTypes.all().map { it.showdownId } }
            .forEach { template ->
                val stack = RadGymsItems.GYM_KEY.defaultInstance.also {
                    it.set(DataComponents.RARITY, Rarity.EPIC)
                    it.set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, template.key)
                }

                entries.accept(stack)
            }
    }

    private fun cacheEntries(
        @Suppress("unused") displayContext: CreativeModeTab.ItemDisplayParameters,
        entries: CreativeModeTab.Output
    ) {
        val caches = listOf(
            RadGymsItems.CACHE_COMMON,
            RadGymsItems.CACHE_UNCOMMON,
            RadGymsItems.CACHE_RARE,
            RadGymsItems.CACHE_EPIC,
        )

        caches.forEach { cache ->
            val stack = ItemStack(cache)
            entries.accept(stack)
            ElementalTypes.all().forEach { type ->
                val stack = cache.defaultInstance.also {
                    it.set(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT, type.showdownId)
                }

                entries.accept(stack)
            }
        }
    }

    fun inject(tabKey: ResourceKey<CreativeModeTab>, injector: Injector) {
        INJECTORS[tabKey]?.invoke(injector)
    }

    @Suppress("unused")
    fun injectorKeys(): Collection<ResourceKey<CreativeModeTab>> = this.INJECTORS.keys


    data class ItemGroupHolder(
        val key: ResourceKey<CreativeModeTab>,
        val displayIconProvider: () -> ItemStack,
        val entryCollector: DisplayItemsGenerator,
        val displayName: Component = Component.translatable(
            "itemGroup.${key.location().namespace}.${key.location().path}"
        )
    )

    private fun create(
        name: ResourceLocation,
        entryCollector: DisplayItemsGenerator,
        displayIconProvider: () -> ItemStack
    ): ResourceKey<CreativeModeTab> {
        val key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), name)
        this.ALL += ItemGroupHolder(key, displayIconProvider, entryCollector)
        return key
    }

    @Suppress("unused")
    private fun inject(
        key: ResourceKey<CreativeModeTab>,
        consumer: (injector: Injector) -> Unit
    ): (injector: Injector) -> Unit {
        this.INJECTORS[key] = consumer
        return consumer
    }

    interface Injector {
        /**
         * Places the given [item] at the start of a creative tab.
         *
         * @param item The [ItemLike] being added at the start of a tab.
         */
        fun putFirst(item: ItemLike)

        /**
         * Places the given [item] before the [target].
         * If the [target] is not present behaves as [putLast].
         *
         * @param item The [ItemLike] being added before [target].
         * @param target The [ItemLike] being targeted.
         */
        fun putBefore(item: ItemLike, target: ItemLike)

        /**
         * Places the given [item] after the [target].
         * If the [target] is not present behaves as [putLast].
         *
         * @param item The [ItemLike] being added after [target].
         * @param target The [ItemLike] being targeted.
         */
        fun putAfter(item: ItemLike, target: ItemLike)

        /**
         * Places the given [item] at the end of a creative tab.
         *
         * @param item The [ItemLike] being added at the end of a tab.
         */
        fun putLast(item: ItemLike)
    }
}
