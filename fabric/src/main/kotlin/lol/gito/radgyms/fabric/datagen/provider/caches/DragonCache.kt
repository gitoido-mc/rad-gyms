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

val DRAGON_CACHE = CacheDTO(
    mapOf(
        Rarity.COMMON.serializedName.lowercase() to mapOf(
            "dreepy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "exeggutor alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "goomy region_bias=hisui" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
        ),
        Rarity.UNCOMMON.serializedName.lowercase() to mapOf(
            "arctibax" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "axew" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "bagon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "dragonair" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "drakloak" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "dratini" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "fraxure" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "frigibax" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "gible" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "goomy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "hakamoo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "jangmoo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "shelgon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
        ),
        Rarity.RARE.serializedName.lowercase() to mapOf(
            "altaria" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "baxcalibur" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "cyclizar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "dragonite" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "druddigon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "gabite" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "garchomp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "goodra" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "haxorus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "sliggoo" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
        ),
        Rarity.EPIC.serializedName.lowercase() to mapOf(
            "arceus multitype=dragon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "dialga orb_forme=origin" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "dialga" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "dragapult" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "eternatus dynamax_form=eternamax" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "giratina orb_forme=origin" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "giratina" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "kommoo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "kyurem" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "latias" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "latios" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "necrozma prism_fusion=ultra" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "palkia orb_forme=origin" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "palkia" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "rayquaza" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "regidrago" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "reshiram" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "roaringmoon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "salamence" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "silvally rks_memory=dragon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "tatsugiri" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "zekrom" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "zygarde" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
        ),
    )
)
