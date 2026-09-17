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
import com.cobblemon.mod.common.api.molang.ObjectValue
import com.cobblemon.mod.common.api.molang.function.NPCMoLangFunctions
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.util.getOrNull
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_INTERACT
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import java.util.UUID

object NPCEntityRGBridge {
    fun init() {
        NPCMoLangFunctions.custom.add { entity ->
            return@add hashMapOf(
                "rg_is_defeated" to fn@{
                    if (!entity.config.map.contains("defeated")) {
                        debug("no defeated flag in trainer config")
                        return@fn DoubleValue.ZERO
                    }
                    val defeated = entity.config.map["defeated"]!!
                    debug("trainer defeat flag value: $defeated")
                    return@fn defeated
                },
                "rg_is_required_defeated" to fn@{
                    if (!entity.config.map.contains("required_trainer")) return@fn DoubleValue.ONE

                    val requiredUuid = entity.config.map["required_trainer"]!!.let {
                        runCatching { return@runCatching UUID.fromString(it.value() as String) }.getOrNull()
                    }

                    if (requiredUuid == null) return@fn DoubleValue.ONE

                    debug("this trainer has required trainer linked, uuid is: $requiredUuid")
                    val required = (entity.level() as ServerLevel).getEntity(requiredUuid) as? NPCEntity?
                        ?: return@fn DoubleValue.ONE

                    when (required.config.map.contains("defeated")) {
                        true -> required.config.map["defeated"]!!
                        else -> DoubleValue.ZERO
                    }
                },
                "rg_is_leader" to fn@{
                    if (!entity.config.map.contains("leader")) return@fn DoubleValue.ZERO
                    entity.config.map["leader"]!!
                },
                "rg_start_battle" to fn@{ params ->
                    val value = params.getOrNull<MoValue>(0) ?: return@fn DoubleValue.ZERO

                    val player = when (value) {
                        is ObjectValue<*> -> value.obj as ServerPlayer

                        is StringValue -> {
                            RadGyms.implementation.server()?.playerList?.getPlayer(UUID.fromString(value.value))!!
                        }

                        else -> {
                            return@fn DoubleValue.ZERO
                        }
                    }

                    TRAINER_INTERACT.post(GymEvents.TrainerInteractEvent(player, entity))

                    return@fn DoubleValue.ONE
                },
            )
        }
    }
}
