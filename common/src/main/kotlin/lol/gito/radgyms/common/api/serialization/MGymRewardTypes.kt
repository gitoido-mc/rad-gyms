/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.reward.AdvancementReward
import lol.gito.radgyms.common.api.dto.reward.CommandReward
import lol.gito.radgyms.common.api.dto.reward.LootTableReward
import lol.gito.radgyms.common.api.dto.reward.PokemonReward
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import lol.gito.radgyms.common.api.enumeration.GymReward
import net.minecraft.core.Registry

object MGymRewardTypes {
    val COMMAND: MGymRewardType<CommandReward> = register(
        GymReward.COMMAND,
        MGymRewardType(CommandReward.CODEC),
    )

    val LOOT_TABLE: MGymRewardType<LootTableReward> = register(
        GymReward.LOOT_TABLE,
        MGymRewardType(LootTableReward.CODEC),
    )

    val POKEMON: MGymRewardType<PokemonReward> = register(
        GymReward.POKEMON,
        MGymRewardType(PokemonReward.CODEC),
    )

    val ADVANCEMENT: MGymRewardType<AdvancementReward> = register(
        GymReward.ADVANCEMENT,
        MGymRewardType(AdvancementReward.CODEC),
    )

    fun <T : RewardInterface> register(type: GymReward, item: MGymRewardType<T>): MGymRewardType<T> = Registry.register(
        MGymRewardType.REGISTRY,
        modId(type.serializedName),
        item,
    )
}
