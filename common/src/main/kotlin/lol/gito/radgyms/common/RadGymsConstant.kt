/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

@file:JvmName("RadGymsConstant")

package lol.gito.radgyms.common

import com.cobblemon.mod.common.api.types.ElementalTypes

/**
 * Purpose of this file is to collect all hardcoded simple values in project in one place
 * Candidates to appear in this file SHOULD be used at least twice across project
 */

const val DEFAULT_GYM_TYPE = "default"

const val COMMANDS_PREFIX = "rg"

const val TELEPORT_PRELOAD_CHUNKS = 4
const val WORLDGEN_FEATURE_RANDOM_SHIFT_BITS = 4
const val GYM_SPACING_IN_DIMENSION = 128

const val EXIT_ROPE_COOLDOWN = 100

const val MIN_POKE_LEVEL = 1
const val MAX_POKE_LEVEL = 100
const val MAX_PARTY_SIZE = 6
const val MAX_PERFECT_IVS = 6
const val MIN_PLAYER_TEAM_SIZE = 3

const val CACHE_STARTER_ENTRY_WEIGHT = 150
const val CACHE_DEFAULT_ENTRY_WEIGHT_COMMON = 400
const val CACHE_DEFAULT_ENTRY_WEIGHT_UNCOMMON = 300
const val CACHE_DEFAULT_ENTRY_WEIGHT_RARE = 200
const val CACHE_DEFAULT_ENTRY_WEIGHT_EPIC = 100

const val REGISTRY_REWARD_TYPE_LOOT_TABLE = "rad_gyms:loot_table"
const val REGISTRY_REWARD_TYPE_POKEMON = "rad_gyms:pokemon"
const val REGISTRY_REWARD_TYPE_COMMAND = "rad_gyms:command"
const val REGISTRY_REWARD_TYPE_ADVANCEMENT = "rad_gyms:advancement"

val defaultElementalTypes =
    setOf(
        ElementalTypes.BUG.showdownId,
        ElementalTypes.DARK.showdownId,
        ElementalTypes.DRAGON.showdownId,
        ElementalTypes.ELECTRIC.showdownId,
        ElementalTypes.FLYING.showdownId,
        ElementalTypes.FIGHTING.showdownId,
        ElementalTypes.FIRE.showdownId,
        ElementalTypes.FLYING.showdownId,
        ElementalTypes.GHOST.showdownId,
        ElementalTypes.GRASS.showdownId,
        ElementalTypes.GROUND.showdownId,
        ElementalTypes.ICE.showdownId,
        ElementalTypes.NORMAL.showdownId,
        ElementalTypes.POISON.showdownId,
        ElementalTypes.PSYCHIC.showdownId,
        ElementalTypes.ROCK.showdownId,
        ElementalTypes.STEEL.showdownId,
        ElementalTypes.WATER.showdownId,
    )
