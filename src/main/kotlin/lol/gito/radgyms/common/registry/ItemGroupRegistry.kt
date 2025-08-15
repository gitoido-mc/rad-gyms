/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.util.annotation.RegisterBlockItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity
import kotlin.reflect.KVisibility
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

object ItemGroupRegistry {
    val GENERAL_GROUP_KEY: RegistryKey<ItemGroup> =
        RegistryKey.of(Registries.ITEM_GROUP.key, modId("general"))
    val KEYS_GROUP_KEY: RegistryKey<ItemGroup> =
        RegistryKey.of(Registries.ITEM_GROUP.key, modId("keys"))
    val CACHES_GROUP_KEY: RegistryKey<ItemGroup> =
        RegistryKey.of(Registries.ITEM_GROUP.key, modId("caches"))

    val GENERAL_GROUP: ItemGroup = FabricItemGroup
        .builder()
        .icon { ItemStack(BlockRegistry.GYM_ENTRANCE.asItem()) }
        .displayName(translatable(modId("group.general").toTranslationKey()))
        .build()

    val KEYS_GROUP: ItemGroup = FabricItemGroup
        .builder()
        .icon { ItemStack(ItemRegistry.GYM_KEY) }
        .displayName(translatable(modId("group.keys").toTranslationKey()))
        .build()

    val CACHES_GROUP: ItemGroup = FabricItemGroup
        .builder()
        .icon { ItemStack(ItemRegistry.CACHE_EPIC) }
        .displayName(translatable(modId("group.caches").toTranslationKey()))
        .build()

    fun register() {
        debug("Registering item groups")
        Registry.register(
            Registries.ITEM_GROUP,
            GENERAL_GROUP_KEY,
            GENERAL_GROUP
        )

        Registry.register(
            Registries.ITEM_GROUP,
            KEYS_GROUP_KEY,
            KEYS_GROUP
        )

        Registry.register(
            Registries.ITEM_GROUP,
            CACHES_GROUP_KEY,
            CACHES_GROUP
        )


        ItemGroupEvents.modifyEntriesEvent(GENERAL_GROUP_KEY).register { itemGroup ->
            itemGroup.add(ItemRegistry.EXIT_ROPE)
            itemGroup.add(ItemRegistry.SHARD_COMMON)
            itemGroup.add(ItemRegistry.SHARD_UNCOMMON)
            itemGroup.add(ItemRegistry.SHARD_RARE)
            itemGroup.add(ItemRegistry.SHARD_EPIC)

            BlockRegistry::class.memberProperties.forEach { property ->
                if (!property.hasAnnotation<RegisterBlockItem>()) return@forEach
                if (property.visibility != KVisibility.PUBLIC) return@forEach
                val block = property.getter.call(BlockRegistry)
                if (block !is Block) return@forEach
                itemGroup.add(block.asItem())
            }
        }

        ItemGroupEvents.modifyEntriesEvent(KEYS_GROUP_KEY).register { itemGroup ->
            val key = ItemRegistry.GYM_KEY
            itemGroup.add(key)
            ElementalTypes.all().forEach {
                val components = ComponentMap.builder()
                components.add(DataComponentTypes.RARITY, Rarity.RARE)
                components.add(DataComponentRegistry.GYM_TYPE_COMPONENT, it.name)

                val stack = ItemStack(key)
                stack.applyComponentsFrom(components.build())
                itemGroup.add(stack)
            }

            GymManager.GYM_TEMPLATES
                .filterNot { it.key == "default" }
                .filterNot { template -> template.key in ElementalTypes.all().map { it.name.lowercase() } }
                .map { it.key }
                .forEach {
                    val components = ComponentMap.builder()
                    components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                    components.add(DataComponentRegistry.GYM_TYPE_COMPONENT, it)

                    val stack = ItemStack(key)
                    stack.applyComponentsFrom(components.build())
                    itemGroup.add(stack)
                }
        }

        ItemGroupEvents.modifyEntriesEvent(CACHES_GROUP_KEY).register { itemGroup ->
            val caches = listOf(
                ItemRegistry.CACHE_COMMON,
                ItemRegistry.CACHE_UNCOMMON,
                ItemRegistry.CACHE_RARE,
                ItemRegistry.CACHE_EPIC,
            )

            caches.forEach { cache ->
                val stack = ItemStack(cache)
                itemGroup.add(stack)
                ElementalTypes.all().forEach {
                    val components = ComponentMap.builder()
                    components.add(DataComponentRegistry.GYM_TYPE_COMPONENT, it.name.lowercase())
                    val stack = ItemStack(cache)
                    stack.applyComponentsFrom(components.build())
                    itemGroup.add(stack)
                }
            }
        }
    }
}