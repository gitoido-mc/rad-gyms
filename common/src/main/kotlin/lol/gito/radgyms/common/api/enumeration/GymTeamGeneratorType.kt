/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.gym.team.AbstractTeamGenerator
import lol.gito.radgyms.common.gym.team.BstTeamGenerator
import lol.gito.radgyms.common.gym.team.ChaoticTeamGenerator

@Serializable
enum class GymTeamGeneratorType {
    @JvmField
    @SerialName("bst")
    BST {
        override val instance: AbstractTeamGenerator = BstTeamGenerator()
    },

    @JvmField
    @SerialName("chaotic")
    CHAOTIC {
        override val instance: AbstractTeamGenerator = ChaoticTeamGenerator()
    };

    abstract val instance: AbstractTeamGenerator
}
