package lol.gito.radgyms.common.api.dto.geospatial

import kotlinx.serialization.Serializable

@Serializable
data class EntityCoordsAndYaw(
    val pos: Coords,
    val yaw: Double,
)
