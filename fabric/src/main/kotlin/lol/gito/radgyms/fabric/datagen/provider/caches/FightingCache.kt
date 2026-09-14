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
                    "tyrogue" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "makuhita" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "meditite" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "riolu" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "croagunk" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "machop" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "mankey" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "timburr" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "crabrawler" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pancham" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "scraggy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "mienfoo" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "farfetchd galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "kubfu" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "machoke" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "combusken" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "gurdurr" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "monferno" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "medicham" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pignite" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "sneasel hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "primeape" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hitmonlee" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hitmonchan" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hitmontop" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "breloom" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sawk" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "throh" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "falinks" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hariyama" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "crabominable" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "grapploct" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "scrafty" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "toxicroak" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "passimian" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pawmot" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tauros paldean" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tauros paldean bull_breed=blaze" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tauros paldean bull_breed=aqua" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pangoro" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "hawlucha" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "flamigo" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "machamp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "conkeldurr" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sirfetchd" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "poliwrath" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mienshao" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sneasler" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gallade" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "lucario" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "emboar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "blaziken" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "chesnaught" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "quaquaval" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "infernape" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "annihilape" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "zapdos galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "buzzwole" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "pheromosa" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "greattusk" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironhands" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "slitherwing" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "cobalion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "terrakion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "virizion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "keldeo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironvaliant" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kommoo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "meloetta" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "marshadow" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zeraora" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zamazenta" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "koraidon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "urshifu wushu_style=single_strike" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "urshifu wushu_style=rapid_strike" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=fighting" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arceus multitype=fighting" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
