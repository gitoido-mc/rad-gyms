/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.cobblemon.molang

import com.bedrockk.molang.runtime.value.DoubleValue
import com.bedrockk.molang.runtime.value.MoValue
import com.bedrockk.molang.runtime.value.StringValue
import com.cobblemon.mod.common.api.molang.MoLangFunctions.npcFunctions
import com.cobblemon.mod.common.api.molang.ObjectValue
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.util.getOrNull
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_INTERACT
import lol.gito.radgyms.common.extension.cobblemon.npc.isDefeated
import lol.gito.radgyms.common.extension.cobblemon.npc.isLeader
import lol.gito.radgyms.common.extension.cobblemon.npc.required
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import java.util.*
import java.util.function.Function as Fn

object NPCEntityRGBridge {
    fun init() {
        npcFunctions.add { entity ->
            return@add hashMapOf(
                "rg_is_defeated" to Fn { _ ->
                    RadGyms.LOGGER.info("checking trainer defeat: ${entity.isDefeated}")
                    return@Fn when(entity.isDefeated) {
                        true -> DoubleValue.ONE
                        false -> DoubleValue.ZERO
                    }
                },
                "rg_is_required_defeated" to Fn { _ ->
                    RadGyms.LOGGER.info("checking required trainer defeat, required uuid is: ${entity.required}")
                    if (entity.required == null) return@Fn DoubleValue.ONE
                    val required = (entity.level() as ServerLevel).getEntity(entity.required!!) as NPCEntity
                    return@Fn when(required.isDefeated) {
                        true -> DoubleValue.ONE
                        false -> DoubleValue.ZERO
                    }
                },
                "rg_is_leader" to Fn { _ ->
                    return@Fn when(entity.isLeader) {
                        true -> DoubleValue.ONE
                        false -> DoubleValue.ZERO
                    }
                },
                "rg_start_battle" to Fn { params ->
                    val value = params.getOrNull<MoValue>(0) ?: return@Fn DoubleValue.ZERO

                    val player = when (value) {
                        is ObjectValue<*> -> value.obj as ServerPlayer
                        is StringValue -> {
                            RadGyms.implementation.server()?.playerList?.getPlayer(UUID.fromString(value.value))!!
                        }

                        else -> {
                            return@Fn DoubleValue.ZERO
                        }
                    }

                    TRAINER_INTERACT.post(GymEvents.TrainerInteractEvent(player, entity))

                    return@Fn DoubleValue.ONE
                },
            )
        }
    }
}
