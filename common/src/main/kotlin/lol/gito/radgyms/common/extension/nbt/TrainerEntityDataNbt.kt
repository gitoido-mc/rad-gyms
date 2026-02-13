package lol.gito.radgyms.common.extension.nbt

import lol.gito.radgyms.common.api.dto.TrainerEntityData
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component


fun CompoundTag.getRadGymsTrainerEntityData(key: String): TrainerEntityData? {
    val tag = this.getCompound(key) ?: return null

    return TrainerEntityData(
        Component.literal(tag.getString("Name")),
        tag.getVec3("RelativePosition")!!,
        tag.getFloat("Yaw")
    )
}

fun CompoundTag.putRadGymsTrainerEntityData(key: String, value: TrainerEntityData) {
    val tag = CompoundTag()

    tag.putString("Name", value.name.string)
    tag.putVec3("RelativePosition", value.relativePosition)
    tag.putFloat("Yaw", value.yaw)

    this.put(key, tag)
}
