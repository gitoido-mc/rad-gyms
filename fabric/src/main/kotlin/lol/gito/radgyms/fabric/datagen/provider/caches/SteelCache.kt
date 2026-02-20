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

val STEEL_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "diglett alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "dugtrio alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "goodra hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "meowth galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sliggoo hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "stunfisk galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "aron" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "beldum" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "bronzor" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cufant" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "doublade" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "honedge" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "klang" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "klink" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "lairon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mawile" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "metang" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "perrserker" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "varoom" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "wormadam bagworm_cloak=trash" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "aegislash" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "aggron" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "bronzong" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "celesteela" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "copperajah" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "duraludon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gholdengo" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "klefki" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "klinklang" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "orthworm" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "revavroom" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "skarmory" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "steelix" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=steel" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "archaludon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "cobalion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "dialga" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "genesect techno_drive=electric" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "genesect techno_drive=fire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "genesect techno_drive=ice" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "genesect techno_drive=water" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironcrown" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "jirachi" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "magearna" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "melmetal" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "meltan" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "metagross" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "necrozma prism_fusion=dusk" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "registeel" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=steel" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zacian crowned" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zamazenta crowned" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
