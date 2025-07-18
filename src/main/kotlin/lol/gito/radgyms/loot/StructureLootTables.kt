/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.loot

import lol.gito.radgyms.RadGyms.modId
import net.minecraft.loot.LootTable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object StructureLootTables {
    val COMMON_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/common"))
    val BUG_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/bug"))
    val DARK_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/dark"))
    val DRAGON_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/dragon"))
    val ELECTRIC_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/electric"))
    val FAIRY_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/fairy"))
    val FIGHTING_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/fighting"))
    val FIRE_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/fire"))
    val FLYING_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/flying"))
    val GHOST_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/ghost"))
    val GRASS_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/grass"))
    val GROUND_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/ground"))
    val ICE_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/ice"))
    val NORMAL_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/normal"))
    val POISON_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/poison"))
    val PSYCHIC_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/psychic"))
    val ROCK_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/rock"))
    val STEEL_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/steel"))
    val WATER_GRAVEL: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("structures/entrance/water"))
}