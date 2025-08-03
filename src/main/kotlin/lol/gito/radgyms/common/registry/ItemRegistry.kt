/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ItemRegistry {
    val GYM_KEY: GymKey = Registry.register(
        Registries.ITEM,
        modId("gym_key"),
        GymKey()
    )

    val EXIT_ROPE: ExitRope = Registry.register(
        Registries.ITEM,
        modId("exit_rope"),
        ExitRope()
    )

    val SHARD_COMMON: CommonPokeShard = Registry.register(
        Registries.ITEM,
        modId("shard_common"),
        CommonPokeShard()
    )

    val SHARD_UNCOMMON: UncommonPokeShard = Registry.register(
        Registries.ITEM,
        modId("shard_uncommon"),
        UncommonPokeShard()
    )

    val SHARD_RARE: RarePokeShard = Registry.register(
        Registries.ITEM,
        modId("shard_rare"),
        RarePokeShard()
    )

    val SHARD_EPIC: EpicPokeShard = Registry.register(
        Registries.ITEM,
        modId("shard_epic"),
        EpicPokeShard()
    )

    val CACHE_COMMON: CommonPokeCache = Registry.register(
        Registries.ITEM,
        modId("cache_common"),
        CommonPokeCache()
    )

    val CACHE_UNCOMMON: UncommonPokeCache = Registry.register(
        Registries.ITEM,
        modId("cache_uncommon"),
        UncommonPokeCache()
    )

    val CACHE_RARE: RarePokeCache = Registry.register(
        Registries.ITEM,
        modId("cache_rare"),
        RarePokeCache()
    )

    val CACHE_EPIC: EpicPokeCache = Registry.register(
        Registries.ITEM,
        modId("cache_epic"),
        EpicPokeCache()
    )

    fun register() {
        debug("Registering item groups")
    }
}