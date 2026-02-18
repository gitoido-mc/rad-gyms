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

val ROCK_CACHE = CacheDTO(
    mapOf(
        Rarity.COMMON.serializedName.lowercase() to mapOf(
            "arcanine hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "growlithe hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "bonsly" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "roggenrola" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "avalugg hisuian" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "rockruff" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "rockruff wolf_form=dusk" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "rockruff wolf_form=midnight" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "rolycoly" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
            "nacli" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON
        ),
        Rarity.UNCOMMON.serializedName.lowercase() to mapOf(
            "amaura" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "anorith" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "archen" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "binacle" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "boldore" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "carkol" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "cranidos" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "geodude" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "glimmet" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "graveler" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "kabuto" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "larvitar" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "lileep" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "naclstack" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "nosepass" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "omanyte" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "onix" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "pupitar" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "shieldon" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "sudowoodo" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
            "tyrunt" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
        ),
        Rarity.RARE.serializedName.lowercase() to mapOf(
            "aerodactyl" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "archeops" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "armaldo" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "aurorus" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "barbaracle" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "bastiodon" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "carbink" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "coalossal" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "cradily" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "garganacl" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "gigalith" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "glimmora" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "golem" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "kabutops" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "klawf" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "lunatone" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "lycanroc wolf_form=dusk" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "lycanroc wolf_form=midnight" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "lycanroc" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "minior" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "nihilego" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "omastar" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "probopass" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "rampardos" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "solrock" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "stakataka" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "stonjourner" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
            "tyrantrum" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
        ),
        Rarity.EPIC.serializedName.lowercase() to mapOf(
            "arceus multitype=rock" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "diancie" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "ironboulder" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "ironthorns" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "ogerpon embody_aspect ogre_mask=cornerstone" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "ogerpon ogre_mask=cornerstone" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "regirock" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "silvally rks_memory=rock" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "terrakion" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
            "tyranitar" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
        ),
    )
)
