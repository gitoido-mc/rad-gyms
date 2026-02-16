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
        val REGISTRY: Registry<MGymRewardType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(modId("gym_reward")),
            Lifecycle.stable()
        )
    }
}
