package lol.gito.radgyms.common.api.dto.gym

import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.core.BlockPos
import java.util.UUID

data class Gym(
    val template: GymTemplate,
    val npcList: List<UUID>,
    val coords: BlockPos,
    val level: Int,
    val type: String
)
