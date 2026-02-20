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

val ELECTRIC_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "blitzle" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "electrike" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "elekid" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "geodude alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "golem alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "graveler alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "helioptile" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "mareep" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pawmi" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pichu region-bias=alola" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pichu" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pikachu region_bias=alola" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "shinx" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tadbulb" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "toxel" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tynamo" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "wattrel" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "yamper" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "dedenne" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "eelektrik" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "emolga" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "flaaffy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "luxio" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "magnemite" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "minun" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "morpeko" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pachirisu" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pawmo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pikachu league_cap=partner" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pikachu" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pincurchin" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "plusle" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "rotom" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "togedemaru" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "voltorb" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "ampharos" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "arctozolt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "bellibolt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "boltund" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "dracozolt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "eelektross" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "electabuzz" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "electivire" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "electrode" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "heliolisk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "jolteon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "kilowattrel" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "luxray" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "magneton" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "magnezone" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "manectric" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "oricorio dance_style=pom_pom" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pawmot" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "raichu" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "toxtricity" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "xurkitree" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "zebstrika" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=electric" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "miraidon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ragingbolt" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "raikou" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "regieleki" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "sandyshocks" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=electric" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapukoko" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "thundurus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zapdos" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zeraora" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
