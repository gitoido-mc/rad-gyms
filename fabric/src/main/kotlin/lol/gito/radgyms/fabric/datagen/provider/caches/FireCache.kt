/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider.caches

import lol.gito.radgyms.common.*
import lol.gito.radgyms.common.cache.CacheDTO
import net.minecraft.world.item.Rarity

val FIRE_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "charcadet" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "magby" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "marowak alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "slugma" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "vulpix" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "castform forecast_form=sunny" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "darumaka" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "fletchinder" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "growlithe" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "litleo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "magcargo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "numel" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "pansear" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "ponyta" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sizzlipede" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "arcanine" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "armarouge" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "blacephalon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "camerupt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "centiskorch" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ceruledge" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "charmander" to CACHE_STARTER_ENTRY_WEIGHT,
                    "chimchar" to CACHE_STARTER_ENTRY_WEIGHT,
                    "cyndaquil region_bias=hisui" to CACHE_STARTER_ENTRY_WEIGHT,
                    "cyndaquil" to CACHE_STARTER_ENTRY_WEIGHT,
                    "darmanitan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "fennekin" to CACHE_STARTER_ENTRY_WEIGHT,
                    "flareon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "fuecoco" to CACHE_STARTER_ENTRY_WEIGHT,
                    "heatmor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "litten" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "magmar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "magmortar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ninetales" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "oricorio" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pyroar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rapidash" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rotom appliance=heat" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scorbunny" to CACHE_STARTER_ENTRY_WEIGHT,
                    "simisear" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "talonflame" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "tepig" to CACHE_STARTER_ENTRY_WEIGHT,
                    "torchic" to CACHE_STARTER_ENTRY_WEIGHT,
                    "torkoal" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "turtonator" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=fire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "entei" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "gougingfire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "groudon reversion_state=primal" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "groudon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "heatran" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "hooh" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironmoth" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "moltres" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ogerpon embody_aspect ogre_mask=hearthflame" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ogerpon ogre_mask=hearthflame" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=fire" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "volcanion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
