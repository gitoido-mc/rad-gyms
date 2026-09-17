/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import lol.gito.radgyms.common.gym.team.BstTeamGenerator
import lol.gito.radgyms.common.gym.team.ChaoticTeamGenerator
import lol.gito.radgyms.common.gym.team.GenericTeamGenerator
import net.minecraft.util.StringRepresentable

enum class GymTeamGeneratorType : StringRepresentable {
    @JvmField
    BST {
        override val instance: GenericTeamGenerator = BstTeamGenerator
    },

    @JvmField
    CHAOTIC {
        override val instance: GenericTeamGenerator = ChaoticTeamGenerator
    }, ;

    override fun getSerializedName(): String = this.name.lowercase()

    abstract val instance: GenericTeamGenerator

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymTeamGeneratorType> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
