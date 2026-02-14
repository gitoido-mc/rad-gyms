/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.exception

sealed class RadGymsException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class RadGymsLevelNotFoundException(message: String) : RadGymsException(message)
class RadGymsSpeciesListEmptyException(message: String): RadGymsException(message)
class RadGymsUnknownBattleAIException(message: String): RadGymsException(message)
