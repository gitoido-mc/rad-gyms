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

object GymLootTables {
    val COMMON_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("gyms/default/common_loot_table"))
    val UNCOMMON_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("gyms/default/uncommon_loot_table"))
    val RARE_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("gyms/default/rare_loot_table"))
    val EPIC_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("gyms/default/epic_loot_table"))
    val SHARED_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, modId("gyms/default/shared_loot_table"))
}