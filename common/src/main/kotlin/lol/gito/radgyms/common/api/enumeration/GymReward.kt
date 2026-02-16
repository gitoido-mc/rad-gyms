package lol.gito.radgyms.common.api.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_COMMAND
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_LOOT_TABLE
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_POKEMON
import net.minecraft.util.StringRepresentable

@Serializable
enum class GymReward : StringRepresentable {
    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_LOOT_TABLE)
    LOOT_TABLE,

    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_COMMAND)
    COMMAND,

    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_POKEMON)
    POKEMON;

    override fun getSerializedName(): String = this.name.lowercase()

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymReward> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
