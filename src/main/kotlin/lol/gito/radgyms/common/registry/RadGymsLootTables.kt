/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable

object RadGymsLootTables {
    // Structure loot tables

    @JvmField
    val COMMON_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/common"))

    @JvmField
    val BUG_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/bug"))

    @JvmField
    val DARK_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/dark"))

    @JvmField
    val DRAGON_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/dragon"))

    @JvmField
    val ELECTRIC_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/electric"))

    @JvmField
    val FAIRY_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/fairy"))

    @JvmField
    val FIGHTING_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/fighting"))

    @JvmField
    val FIRE_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/fire"))

    @JvmField
    val FLYING_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/flying"))

    @JvmField
    val GHOST_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/ghost"))

    @JvmField
    val GRASS_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/grass"))

    @JvmField
    val GROUND_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/ground"))

    @JvmField
    val ICE_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/ice"))

    @JvmField
    val NORMAL_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/normal"))

    @JvmField
    val POISON_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/poison"))

    @JvmField
    val PSYCHIC_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/psychic"))

    @JvmField
    val ROCK_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/rock"))

    @JvmField
    val STEEL_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/steel"))

    @JvmField
    val WATER_GRAVEL: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("structures/entrance/water"))

    // Cache loot tables

    @JvmField
    val COMMON_LOOT_TABLE: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("gyms/default/common_loot_table"))

    @JvmField
    val UNCOMMON_LOOT_TABLE: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("gyms/default/uncommon_loot_table"))

    @JvmField
    val RARE_LOOT_TABLE: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("gyms/default/rare_loot_table"))

    @JvmField
    val EPIC_LOOT_TABLE: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("gyms/default/epic_loot_table"))

    @JvmField
    val SHARED_LOOT_TABLE: ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, RadGyms.modId("gyms/default/shared_loot_table"))
}