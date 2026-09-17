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

val ICE_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "bergmite region_bias=hisui" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "darmanitan galarian blazing_mode=zen" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "darmanitan galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "darumaka galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "mrmime galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "ninetales alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sandshrew alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sandslash alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "smoochum" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "snom" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "spheal" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "swinub" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "vulpix alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "bergmite" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "castform forecast_form=snowy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cetoddle" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cubchoo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "delibird" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sealeo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "snorunt" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vanillish" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vanillite" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "avalugg" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "beartic" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "cetitan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "cryogonal" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "eiscue penguin_head=noice_face" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "eiscue" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "froslass" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "frosmoth" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "glaceon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "glalie" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "jynx" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mamoswine" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mrrime" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "piloswine" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rotom appliance=frost" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "vanilluxe" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "walrein" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=ice" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "articuno" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "calyrex king_steed=ice" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "glastrier" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironbundle" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kyurem absofusion=black" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kyurem absofusion=white" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "regice" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=ice" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
