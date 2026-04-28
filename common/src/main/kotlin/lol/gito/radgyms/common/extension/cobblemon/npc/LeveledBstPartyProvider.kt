/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.cobblemon.npc

import com.cobblemon.mod.common.api.npc.NPCPartyProvider
import com.cobblemon.mod.common.api.storage.party.NPCPartyStore
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.google.gson.JsonElement
import net.minecraft.server.level.ServerPlayer

class LeveledBstPartyProvider : NPCPartyProvider {
    companion object {
        const val TYPE = "rg_bst"

        fun init() {
            NPCPartyProvider.types[TYPE] = { LeveledBstPartyProvider() }
        }
    }

    @Transient
    override val type = TYPE
    override val isStatic: Boolean = false

    val elementalTypes: List<ElementalType> = mutableListOf()

    override fun provide(npc: NPCEntity, level: Int, players: List<ServerPlayer>): NPCPartyStore {

    }

    override fun loadFromJSON(json: JsonElement) {

    }
}
