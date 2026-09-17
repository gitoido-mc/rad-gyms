/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider.caches

import lol.gito.radgyms.common.CACHE_DEFAULT_ENTRY_WEIGHT_COMMON
import lol.gito.radgyms.common.CACHE_DEFAULT_ENTRY_WEIGHT_EPIC
import lol.gito.radgyms.common.CACHE_DEFAULT_ENTRY_WEIGHT_RARE
import lol.gito.radgyms.common.CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON
import lol.gito.radgyms.common.cache.CacheDTO
import net.minecraft.world.item.Rarity

val GROUND_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "cubone region_bias=alola" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "diglett" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sandile" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "trapinch" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "yamask galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "baltoy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cubone" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "drilbur" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dugtrio" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "gligar" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "golett" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hippopotas" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "krokorok" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "marowak" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mudbray" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "phanpy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "rhyhorn" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sandshrew" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "silicobra" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "toedscool" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vibrava" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "wormadam bagworm_cloak=sandy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "claydol" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "donphan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "excadrill" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "flygon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gastrodon sea=east" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gliscor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "golurk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hippowdon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "krookodile" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mudsdale" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rhydon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rhyperior" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "runerigus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sandaconda" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sandslash" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "stunfisk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "toedscruel" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ursaluna" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=ground" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "greattusk" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "groudon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "irontreads" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "landorus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=ground" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde percent_cells=10" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde percent_cells=complete" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde power_construct percent_cells=10" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde power_construct percent_cells=50" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
