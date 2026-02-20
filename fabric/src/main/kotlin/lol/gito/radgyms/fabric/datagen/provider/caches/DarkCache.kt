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
                    "grimer alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "impidimp" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "inkay" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "linoone galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "meowth alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "muk alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nickit" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "persian alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "poochyena" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "purrloin" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "qwilfish hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "raticate alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rattata alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "samurott hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "zigzagoon galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "deino" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "houndour" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "liepard" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "maschiff" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mightyena" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "morgrem" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "murkrow" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pawniard" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sableye" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "scraggy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sneasel" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vullaby" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "zorua" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "zweilous" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "absol" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "bisharp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "greninja battle_bond=bond" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "grimmsnarl" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "guzzlord" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "honchkrow" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "houndoom" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "kingambit" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mabosstiff" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "malamar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mandibuzz" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "obstagoon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "overqwil" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scrafty" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "thievul" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "umbreon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "weavile" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "zoroark" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=dark" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "chienpao" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "chiyu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "darkrai" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "greninja battle_bond=ash" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hoopa unbound" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hydreigon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironjugulis" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "moltres galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=dark" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tinglu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "wochien" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "yveltal" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zarude" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
