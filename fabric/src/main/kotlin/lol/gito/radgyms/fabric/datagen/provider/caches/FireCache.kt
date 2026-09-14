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
import lol.gito.radgyms.common.CACHE_STARTER_ENTRY_WEIGHT
import lol.gito.radgyms.common.cache.CacheDTO
import net.minecraft.world.item.Rarity

val FIRE_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "slugma" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "charcadet" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "litwick" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "numel" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "sizzlipede" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "darumaka" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pansear" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "litten" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "salandit" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "houndour" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "growlithe" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "growlithe hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "larvesta" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "magby" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "litleo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "lampent" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "fletchinder" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "ponyta" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "carkol" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "marowak alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "magcargo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "camerupt" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "torkoal" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "oricorio dance_style=baile" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "darmanitan" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "salazzle" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "heatmor" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "turtonator" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "scovillain" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    // Starters
                    "fennekin" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "tepig" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "charmander" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "cyndaquil" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "chimchar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "torchic" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scorbunny" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "fuecoco" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    // Starters
                    "tauros paldean bull_breed=blaze" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "simisear" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "talonflame" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "houndoom" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rapidash" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ninetales" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pyroar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "coalossal" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rotom-heat" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "chandelure" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "flareon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "centiskorch" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "armarouge" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ceruledge" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "magmortar" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "volcarona" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arcanine" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arcanine hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "blacephalon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "chiyu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "victini" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ho-oh" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "reshiram" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arceus multitype=fire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "entei" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "gougingfire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "heatran" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hooh" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironmoth" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "moltres" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ogerpon ogre_mask=hearthflame" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=fire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "volcanion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
