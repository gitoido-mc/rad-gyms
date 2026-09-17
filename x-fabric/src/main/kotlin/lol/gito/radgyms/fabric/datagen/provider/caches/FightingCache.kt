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

val FIGHTING_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "decidueye hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "farfetchd galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "lilligant hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "makuhita" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "meditite" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "riolu" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sneasel hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tauros paldean bull_breed=aqua" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tauros paldean bull_breed=blaze" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tauros paldean" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tyrogue" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "clobbopus" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "crabrawler" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "gurdurr" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "machoke" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "machop" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mankey" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "medicham" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mienfoo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pancham" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "timburr" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "annihilape" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "conkeldurr" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "crabominable" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "falinks" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "grapploct" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hariyama" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hawlucha" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hitmonchan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hitmonlee" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hitmontop" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "lucario" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "machamp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mienshao" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pangoro" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "passimian" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "primeape" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sawk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sirfetchd" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sneasler" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "throh" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=fighting" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironhands" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "keldeo sword_form=resolute" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "koraidon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kubfu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "marshadow" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "meloetta song_forme=pirouette" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=fighting" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "urshifu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zamazenta" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zapdos galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
