/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.pokecache

import kotlinx.serialization.Serializable


@Serializable
class CacheDTO(
    val common: Map<String, Int>,
    val uncommon: Map<String, Int>,
    val rare: Map<String, Int>,
    val epic: Map<String, Int>,
) {
}
