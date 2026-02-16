package lol.gito.radgyms.common.api.serialization

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.reward.*
import lol.gito.radgyms.common.api.enumeration.GymReward
import net.minecraft.core.Registry

object MGymRewardTypes {
    val COMMAND: MGymRewardType<CommandReward> = register(
        GymReward.COMMAND,
        MGymRewardType(CommandReward.CODEC)
    )

    val LOOT_TABLE: MGymRewardType<LootTableReward> = register(
        GymReward.LOOT_TABLE,
        MGymRewardType(LootTableReward.CODEC)
    )

    val POKEMON: MGymRewardType<PokemonReward> = register(
        GymReward.POKEMON,
        MGymRewardType(PokemonReward.CODEC)
    )

    val ADVANCEMENT: MGymRewardType<AdvancementReward> = register(
        GymReward.ADVANCEMENT,
        MGymRewardType(AdvancementReward.CODEC)
    )

    fun <T : RewardInterface> register(type: GymReward, item: MGymRewardType<T>): MGymRewardType<T> = Registry.register(
        MGymRewardType.REGISTRY,
        modId(type.serializedName),
        item
    )
}
