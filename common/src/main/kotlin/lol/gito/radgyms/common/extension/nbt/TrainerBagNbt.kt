package lol.gito.radgyms.common.extension.nbt

import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsTrainerBag(key: String): MutableList<BagItemModel> {
    val nbt = this.getCompound(key)

    return nbt.allKeys.map {
        BagItemModel(it, nbt.getInt(it))
    }.toMutableList()
}

fun CompoundTag.putRadGymsTrainerBag(key: String, value: MutableList<BagItemModel>) {
    val nbt = CompoundTag()

    value.forEach {
        nbt.putInt(it.item, it.quantity)
    }

    this.put(key, nbt)
}
