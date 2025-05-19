package lol.gito.radgyms.block.state

import com.cobblemon.mod.common.api.scheduling.SchedulingTracker
import com.cobblemon.mod.common.client.render.models.blockbench.PosableState

class GymEntranceState: PosableState() {
    override fun getEntity() = null

    init {
        setPose("idle")
    }

    override fun updatePartialTicks(partialTicks: Float) {
        this.currentPartialTicks = partialTicks
    }

    override val schedulingTracker: SchedulingTracker = SchedulingTracker()
}
