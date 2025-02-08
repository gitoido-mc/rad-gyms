package lol.gito.radgyms.loot

import lol.gito.radgyms.RadGyms
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object GymLootTables {
    val COMMON_LOOT_TABLE =  RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modIdentifier("gyms/default/common_loot"));
    val UNCOMMON_LOOT_TABLE =  RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modIdentifier("gyms/default/uncommon_loot"));
    val RARE_LOOT_TABLE =  RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modIdentifier("gyms/default/rare_loot"));
    val EPIC_LOOT_TABLE =  RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modIdentifier("gyms/default/epic_loot"));
    val SHARED_LOOT_TABLE =  RegistryKey.of(RegistryKeys.LOOT_TABLE, RadGyms.modIdentifier("gyms/default/shared_loot"));
}