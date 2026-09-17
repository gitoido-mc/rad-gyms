/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.entity.npc.NPCEntity
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.registry.RadGymsStats.getStat
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB

object GymLeaveHandler {
    @JvmStatic
    fun execute(event: GymEvents.GymLeaveEvent) {
        debug("gym leave triggered")

        event.gym?.let {
            val bounds = 128
            val corners = listOf(
                event.gym.coords,
                event.gym.coords.north(bounds).east(bounds).above(bounds),
            )

            event.player.level()
                .getEntitiesOfClass(
                    NPCEntity::class.java,
                    AABB.of(BoundingBox.encapsulatingPositions(corners).get()),
                ) { entity -> event.gym.npcList.contains(entity.uuid) }
                .forEach { it.discard() }
        }

        when (event.completed) {
            true -> event.player.awardStat(getStat(RadGyms.statistics.GYMS_BEATEN))

            null -> Unit

            false -> {
                GymTeardownService.destructGym(event.player, removeCoords = false)
                event.player.awardStat(getStat(RadGyms.statistics.GYMS_FAILED))
                if (event.usedRope == true) event.player.awardStat(getStat(RadGyms.statistics.ROPES_USED))
            }
        }

        GymTeardownService.handleGymLeave(event.player)
    }
}
