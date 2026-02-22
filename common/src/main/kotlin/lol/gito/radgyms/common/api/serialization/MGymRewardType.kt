/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

data class MGymRewardType<T : RewardInterface>(val codec: MapCodec<T>?) {
    companion object {
        val REGISTRY: Registry<MGymRewardType<*>> =
            MappedRegistry(
                ResourceKey.createRegistryKey(modId("gym_reward")),
                Lifecycle.stable(),
            )
    }
}
