/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms
import net.minecraft.loot.LootTable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object GymLootTableRegistry {
    val COMMON_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modId("gyms/default/common_loot_table"))
    val UNCOMMON_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modId("gyms/default/uncommon_loot_table"))
    val RARE_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modId("gyms/default/rare_loot_table"))
    val EPIC_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modId("gyms/default/epic_loot_table"))
    val SHARED_LOOT_TABLE: RegistryKey<LootTable> =
        RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modId("gyms/default/shared_loot_table"))
}