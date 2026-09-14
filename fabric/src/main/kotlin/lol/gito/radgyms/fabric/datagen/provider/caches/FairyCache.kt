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

val FAIRY_CACHE =
    CacheDTO(
        mapOf(
            Rarity.COMMON.serializedName.lowercase() to
                mapOf(
                    "azurill" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "ralts" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "igglybuff" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "cleffa" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "togepi" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "marill" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "impidimp" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "jigglypuff" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "milcery" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "kirlia" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "cottonee" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "morelull" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "snubbull" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=blue" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=orange" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=pink" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=red" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=white" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "flabebe flower=yellow" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                    "cutiefly" to CACHE_DEFAULT_ENTRY_WEIGHT_COMMON,
                ),
            Rarity.UNCOMMON.serializedName.lowercase() to
                mapOf(
                    "spritzee" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mimejr" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mimejr region_bias=galar" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "fidough" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "clefairy" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "swirlix" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "morgrem" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=blue" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=orange" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=pink" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=red" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=white" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "floette flower=yellow" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "tinkatuff" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "mawile" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "togetic" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "shiinotic" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "azumarill" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "dedenne" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "wigglytuff" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                    "granbull" to CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON,
                ),
            Rarity.RARE.serializedName.lowercase() to
                run {
                    val rare =
                        mutableMapOf(
                            "mrmime" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "aromatisse" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "ribombee" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "klefki" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "mimikyu" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "dachsbun" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "slurpuff" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "whimsicott" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "clefable" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "comfey" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "weezing galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "carbink" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "rapidash galarian" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "ninetales alolan" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "tinkaton" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "hatterene" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "grimmsnarl" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                            "gardevoir" to CACHE_DEFAULT_ENTRY_WEIGHT_RARE,
                        )

                    val alcremieCream =
                        listOf(
                            "caramel_swirl",
                            "lemon",
                            "matcha",
                            "mint",
                            "rainbow_swirl",
                            "ruby",
                            "ruby_swirl",
                            "salted",
                            "vanilla",
                        )

                    val alcremieDecoration =
                        listOf(
                            "berry",
                            "clover",
                            "flower",
                            "love",
                            "ribbon",
                            "star",
                            "strawberry",
                        )

                    alcremieCream.forEach { cream ->
                        alcremieDecoration.forEach { decoration ->
                            rare["alcremie cream=$cream decoration=$decoration"] = CACHE_DEFAULT_ENTRY_WEIGHT_RARE
                        }
                    }

                    return@run rare
                },
            Rarity.EPIC.serializedName.lowercase() to
                mapOf(
                    "sylveon" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "primarina" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "togekiss" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapukoko" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapulele" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapubulu" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "tapufini" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "screamtail" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "enamorus" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "ironvaliant" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "diancie" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "magearna" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "zacian" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "xerneas" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=blue" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=orange" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=pink" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=red" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=white" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "florges flower=yellow" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "magearna paint_color=original" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "silvally rks_memory=fairy" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "floette flower=eternal" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                    "arceus multitype=fairy" to CACHE_DEFAULT_ENTRY_WEIGHT_EPIC,
                ),
        ),
    )
