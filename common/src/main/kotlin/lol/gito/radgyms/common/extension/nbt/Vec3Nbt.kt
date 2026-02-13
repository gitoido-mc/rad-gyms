package lol.gito.radgyms.common.extension.nbt

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

fun CompoundTag.getVec3(key: String): Vec3? {
    val nbt = this.getCompound(key) ?: return null

    return Vec3(
        nbt.getDouble("x"),
        nbt.getDouble("y"),
        nbt.getDouble("z"),
    )
}

fun CompoundTag.putVec3(key: String, value: Vec3) {
    val nbt = CompoundTag()

    nbt.putDouble("x", value.x)
    nbt.putDouble("y", value.y)
    nbt.putDouble("z", value.y)

    this.put(key, nbt)
}
