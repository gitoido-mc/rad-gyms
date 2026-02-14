package lol.gito.radgyms.common.api.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.StringRepresentable

@Serializable
enum class GymReward : StringRepresentable {
    @JvmField
    @SerialName("loot_table")
    LOOT_TABLE,

    @JvmField
    @SerialName("command")
    COMMAND,

    @JvmField
    @SerialName("pokemon")
    POKEMON;

    override fun getSerializedName(): String = this.name.lowercase()

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymReward> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
