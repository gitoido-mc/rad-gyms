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

val BUG_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "blipbug" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "burmy bagworm_cloak=sandy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "burmy bagworm_cloak=trash" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "burmy" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "cascoon" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "caterpie" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "combee" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "kakuna" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "kricketot" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "ledyba" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "metapod" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nincada" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "nymble" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "paras" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "pineco" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "rellor" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "scatterbug" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "shedinja" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "silcoon" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "spewpa" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "spinarak" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "surskit" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "tarountula" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "venipede" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "weedle" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "wimpod" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "wurmple" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "ariados" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "beautifly" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "beedrill" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "butterfree" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "charjabug" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "cutiefly" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dottler" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dustox" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dwebble" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "grubbin" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "illumise" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "joltik" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "karrablast" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "kricketune" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "larvesta" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "ledian" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mothim" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "parasect" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "sewaddle" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "shelmet" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "spidops" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "swadloon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "venonat" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "vivillon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "volbeat" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "whirlipede" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "wormadam" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "yanma" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                mapOf(
                    "accelgor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "buzzwole" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "crustle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "durant" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "escavalier" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "forretress" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "galvantula" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "golisopod" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "heracross" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "kleavor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "leavanny" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "lokix" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "masquerain" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ninjask" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "orbeetle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pheromosa" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "pinsir" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "rabsca" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "ribombee" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scizor" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scolipede" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "scyther" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "shuckle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "venomoth" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "vespiquen" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "vikavolt" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "volcarona" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                    "yanmega" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                ),
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "arceus multitype=bug" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "genesect" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=bug" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "slitherwing" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
