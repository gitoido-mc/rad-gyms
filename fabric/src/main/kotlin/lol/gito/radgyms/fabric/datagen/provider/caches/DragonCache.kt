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

val DRAGON_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "noibat" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "applin" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "dreepy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "bagon" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "deino" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "dratini" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "gible" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "goomy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "jangmoo" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "axew" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "frigibax" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "vibrava" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tyrunt" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "drakloak" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "fraxure" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "gabite" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "dragonair" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "hakamoo" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "shelgon" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "zweilous" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "arctibax" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sliggoo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sliggoo hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tatsugiri tatsugiri_texture=curly" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tatsugiri tatsugiri_texture=droopy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tatsugiri tatsugiri_texture=stretchy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "appletun" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dipplin" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "drampa" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "druddigon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "flapple" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "turtonator" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "altaria" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dragalge" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cyclizar" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dracozolt" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dracovish" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "flygon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tyrantrum" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "exeggutor alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "duraludon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "noivern" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "haxorus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hydrapple" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "kingdra" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "naganadel" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "guzzlord" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "regidrago" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gouging fire" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "raging bolt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "roaring moon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "walking wake" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "archaludon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "baxcalibur" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "dragapult" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "dragonite" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "garchomp" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "hydreigon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "regidrago" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "gougingfire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "roaringmoon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ragingbolt" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "walkingwake" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "archaludon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "dragapult" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "baxcalibur" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "dragonite" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "garchomp" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "goodra" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "goodra hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kommoo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "latias" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "latios" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "salamence" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde percent_cells=10" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde percent_cells=10 power_construct=true" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zygarde percent_cells=50 power_construct=true" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "kyurem" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "koraidon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "miraidon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "dialga" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "palkia" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "giratina" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "rayquaza" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "reshiram" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zekrom" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "eternatus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=dragon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arceus multitype=dragon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
