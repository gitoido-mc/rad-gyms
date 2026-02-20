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

val POISON_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "ekans" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "koffing region_bias=galar" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nidoranf" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nidoranm" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "shroodle" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "slowbro galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "slowking galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "wooper paldean" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "zubat" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "arbok" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "clodsire" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "croagunk" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "grimer" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "gulpin" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "koffing" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mareanie" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "nidorina" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "nidorino" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "salandit" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "skorupi" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "skrelp" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "stunky" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "trubbish" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "crobat" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "dragalge" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "drapion" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "garbodor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "golbat" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "grafaiai" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "muk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "naganadel" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "nidoking" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "nidoqueen" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "poipole" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "salazzle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "seviper" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "skuntank" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "swalot" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "toxapex" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "toxicroak" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "toxtricity punk_form=low_key" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "weezing" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=poison" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "eternatus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "fezandipiti" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "munkidori" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "okidogi" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "pecharunt" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=poison" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
