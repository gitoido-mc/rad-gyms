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

val FLYING_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "dartrix region_bias=hisui" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "noibat" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rookidee" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rowlet region_bias=hisui" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rufflet region_bias=hisui" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "corvisquire" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "minior" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "squawkabilly squawkabilly_color=blue" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "squawkabilly squawkabilly_color=gray" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "squawkabilly squawkabilly_color=yellow" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vivillon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "bombirdier" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "corviknight" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "cramorant" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "flamigo" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "noivern" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rotom appliance=fan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=flying" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "enamorus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "landorus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "shaymin" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=flying" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "thundurus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tornadus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
