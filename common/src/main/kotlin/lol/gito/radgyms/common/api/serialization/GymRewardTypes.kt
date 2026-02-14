package lol.gito.radgyms.common.api.serialization

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.reward.CommandReward
import lol.gito.radgyms.common.api.dto.reward.LootTableReward
import lol.gito.radgyms.common.api.dto.reward.PokemonReward
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import lol.gito.radgyms.common.api.enumeration.GymReward
import net.minecraft.core.Registry

object GymRewardTypes {
    val COMMAND: GymRewardType<CommandReward> = register(
        GymReward.COMMAND,
        GymRewardType(CommandReward.CODEC)
    )

    val LOOT_TABLE: GymRewardType<LootTableReward> = register(
        GymReward.LOOT_TABLE,
        GymRewardType(LootTableReward.CODEC)
    )

    val POKEMON: GymRewardType<PokemonReward> = register(
        GymReward.POKEMON,
        GymRewardType(PokemonReward.CODEC)
    )

    fun <T : RewardInterface> register(type: GymReward, item: GymRewardType<T>): GymRewardType<T> = Registry.register(
        GymRewardType.REGISTRY,
        modId(type.serializedName),
        item
    )
}
