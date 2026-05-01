/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension.cobblemon.npc

import com.cobblemon.mod.common.entity.npc.NPCEntity
import lol.gito.radgyms.common.ASPECT_DEFEATED
import lol.gito.radgyms.common.ASPECT_LEADER


val NPCEntity.isDefeated: Boolean
    get() = this.aspects.contains(ASPECT_DEFEATED)

val NPCEntity.isLeader: Boolean
    get() = this.aspects.contains(ASPECT_LEADER)
