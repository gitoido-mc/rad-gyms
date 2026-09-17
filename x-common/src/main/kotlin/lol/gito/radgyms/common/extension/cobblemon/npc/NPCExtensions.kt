/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.cobblemon.npc

import com.cobblemon.mod.common.entity.npc.NPCEntity
import lol.gito.radgyms.common.DATA_DEFEATED
import lol.gito.radgyms.common.DATA_LEADER
import lol.gito.radgyms.common.DATA_REQUIRED
import java.util.UUID

val NPCEntity.isDefeated: Boolean
    get() = this.data.map.contains(DATA_DEFEATED) && this.data.map[DATA_DEFEATED]?.asDouble() == 1.0

val NPCEntity.isLeader: Boolean
    get() = this.data.map.contains(DATA_LEADER) && this.data.map[DATA_LEADER]?.asDouble() == 1.0

val NPCEntity.required: UUID?
    get() = when (this.data.map.contains(DATA_REQUIRED)) {
        true -> UUID.fromString(this.data.map[DATA_REQUIRED]!!.asString())
        false -> null
    }
