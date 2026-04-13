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
import com.cobblemon.mod.common.util.getOrNull
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_INTERACT
import net.minecraft.server.level.ServerPlayer
import java.util.*

object NPCEntityRGBridge {
    fun init() {
        npcFunctions.add { entity ->
            return@add hashMapOf(
                "start_rg_battle" to java.util.function.Function { params ->
                    val value = params.getOrNull<MoValue>(0) ?: return@Function DoubleValue.ZERO

                    val player = when (value) {
                        is ObjectValue<*> -> value.obj as ServerPlayer
                        is StringValue -> {
                            RadGyms.implementation.server()?.playerList?.getPlayer(UUID.fromString(value.value))!!
                        }

                        else -> {
                            return@Function DoubleValue.ZERO
                        }
                    }

                    TRAINER_INTERACT.post(GymEvents.TrainerInteractEvent(player, entity))

                    return@Function DoubleValue.ONE
                },
            )
        }
    }
}
