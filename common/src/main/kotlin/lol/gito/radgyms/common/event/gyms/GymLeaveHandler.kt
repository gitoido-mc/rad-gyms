/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.gym.GymTeleportScheduler
import lol.gito.radgyms.common.registry.RadGymsStats.getStat
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB

object GymLeaveHandler {
    const val AABB_OFFSET = 64

    @JvmStatic
    fun execute(event: GymEvents.GymLeaveEvent) {
        debug("gym leave triggered")

        if (event.gym != null) {
            @Suppress("USELESS_CAST")
            event.player.level().getEntitiesOfClass(
                Trainer::class.java,
                AABB.of(
                    BoundingBox.encapsulatingPositions(listOf(
                        event.gym.coords,
                        event.gym.coords.north(AABB_OFFSET).east(AABB_OFFSET).above(AABB_OFFSET),
                    )).get()
                )
            ).forEach { it.discard() }
        }

        if (event.completed == false) {
            GymTeardownService.destructGym(event.player, removeCoords = false)
            event.player.awardStat(getStat(RadGyms.statistics.GYMS_FAILED))
            if (event.usedRope == true) {
                event.player.awardStat(getStat(RadGyms.statistics.ROPES_USED))
            }
        } else {
            event.player.awardStat(getStat(RadGyms.statistics.GYMS_BEATEN))
        }

        GymTeardownService
            .withTeleportScheduler(GymTeleportScheduler())
            .handleGymLeave(event.player)
    }
}
