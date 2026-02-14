package lol.gito.radgyms.common.api.dto.geospatial

import kotlinx.serialization.Serializable
import net.minecraft.world.phys.Vec3

@Serializable
data class Coords(val x: Double, val y: Double, val z: Double) {
    fun toVec3D(): Vec3 = Vec3(this.x, this.y, this.z)
}
