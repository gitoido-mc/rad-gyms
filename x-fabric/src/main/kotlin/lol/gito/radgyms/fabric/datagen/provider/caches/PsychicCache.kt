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

val PSYCHIC_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "braviary hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "chingling" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "exeggcute region_bias=alola" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flittle" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "gothita" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "hatenna" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "kirlia" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "mimejr" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "munna" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "ponyta galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "raichu alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "ralts" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rapidash galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "slowpoke galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "solosis" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "wynaut" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "abra" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "drowzee" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "duosion" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "elgyem" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "espurr" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "gothorita" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "hattrem" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "kadabra" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "natu" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "spoink" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "swoobat" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "unown" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "wobbuffet" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "woobat" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "alakazam" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "beheeyem" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "chimecho" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "darmanitan blazing_mode=zen" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "espathra" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "espeon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gallade" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gardevoir" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "gothitelle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "grumpig" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hatterene" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "hypno" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "indeedee" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "meowstic" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "mrmime" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "musharna" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "oricorio dance_style=pa'u" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "reuniclus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "sigilyph" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "xatu" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=psychic" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "articuno galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "azelf" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "calyrex" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "celebi" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "cosmoem" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "cosmog" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "cresselia" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "deoxys meteorite_forme=attack" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "deoxys meteorite_forme=defense" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "deoxys meteorite_forme=speed" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "deoxys" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hoopa" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "lugia" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "lunala" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "mesprit" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "mew" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "mewtwo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "necrozma" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=psychic" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "solgaleo" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapulele" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "uxie" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "victini" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
