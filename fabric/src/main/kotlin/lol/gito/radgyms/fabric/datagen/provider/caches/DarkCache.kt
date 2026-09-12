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

val DARK_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "poochyena" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "zigzagoon galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nickit" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rattata alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "impidimp" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "purrloin" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "inkay" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "meowth alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sandile" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "deino" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "carvanha" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "grimer alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "houndour" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "zorua" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "maschiff" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "nuzleaf" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pawniard" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "krokorok" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "morgrem" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vullaby" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sableye" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "raticate alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "zweilous" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "linoone galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sneasel" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "morpeko" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "persian alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "qwilfish hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "liepard" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "lokix" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "thievul" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "absol" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "crawdaunt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "cacturne" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "skuntank" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "malamar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "spiritomb" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "bombirdier" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scrafty" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "bisharp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pangoro" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "muk alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "drapion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "honkchow" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "mabostiff" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "weavile" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zoroark" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "mandibuzz" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "grimmsnarl" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "overqwil" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "krookodile" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "obstagoon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "samurott hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "greninja" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "incineroar" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "meowscarada" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "urshifu wushu_style=single_strike" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "guzzlord" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=dark" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "brutebonnet" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironjugulis" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "wochien" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "chienpao" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tinglu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "chiyu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "moltres galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "roaringmoon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "darkrai" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hydreigon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zarude" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "yveltal" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hoopa djinn_state=unbound" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arceus multitype=dark" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
