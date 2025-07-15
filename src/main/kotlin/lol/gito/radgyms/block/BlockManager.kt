/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.block

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler
import lol.gito.radgyms.RadGyms.MOD_ID
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.block.entity.BlockEntityRegistry

object BlockManager {
    fun register() {
        debug("Registering blocks and block entities")
        BlockEntityRegistry.register()
        FieldRegistrationHandler.register(BlockRegistry::class.java, MOD_ID, false)
    }
}
