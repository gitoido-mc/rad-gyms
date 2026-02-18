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

val GHOST_CACHE = CacheDTO(
    mapOf(
        Rarity.COMMON.serializedName.lowercase() to mapOf(
            "corsola galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "duskull" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "greavard" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "litwick" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "shuppet" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "typhlosion hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
        ),
        Rarity.UNCOMMON.serializedName.lowercase() to mapOf(
            "drifloon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "gastly" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "gimmighoul roaming" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "gimmighoul" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "haunter" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "lampent" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "misdreavus" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "phantump" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "poltchageist matcha_authenticity=artisan" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "poltchageist matcha_authenticity=counterfeit" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "poltchageist matcha_authenticity=masterpiece" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "poltchageist matcha_authenticity=unremarkable" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "pumpkaboo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "sandygast" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "sinistea tea_authencity=antique" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "sinistea" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "yamask" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
        ),
        Rarity.RARE.serializedName.lowercase() to mapOf(
            "aegislash" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "banette" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "basculegion female" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "chandelure" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "cofagrigus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "cursola" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "dhelmise" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "drifblim" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "dusclops" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "dusknoir" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "gengar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "gourgeist" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "houndstone" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "mimikyu" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "mismagius" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "oricorio dance_style=sensu" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "palossand" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "polteageist tea_authencity=antique" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "polteageist" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "sinistcha matcha_authenticity=artisan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "sinistcha matcha_authenticity=counterfeit" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "sinistcha matcha_authenticity=masterpiece" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "sinistcha matcha_authenticity=unremarkable" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "spiritomb" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "trevenant" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
        ),
        Rarity.EPIC.serializedName.lowercase() to mapOf(
            "arceus multitype=ghost" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "calyrex king_steed=shadow" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "fluttermane" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "giratina" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "necrozma prism_fusion=dawn" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "silvally rks_memory=ghost" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "spectrier" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
        ),
    )
)
