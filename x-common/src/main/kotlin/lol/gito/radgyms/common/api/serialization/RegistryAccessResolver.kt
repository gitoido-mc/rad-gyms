
/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package lol.gito.radgyms.common.api.serialization

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.util.server
import lol.gito.radgyms.common.RadGyms
import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess

/**
 * Utility for getting RegistryAccess with the correct client/server registry access.
 * Used when as a fallback when manually encoding/decoding registry-backed values (e.g., items, predicates).
 */
object RegistryAccessResolver {
    fun getAccess(): RegistryAccess {
        try {
            RadGyms.implementation
        } catch (_: UninitializedPropertyAccessException) {
            return RegistryAccess.EMPTY
        }
        if (RadGyms.implementation.environment() == Environment.CLIENT) {
            if (Minecraft.getInstance()?.level != null) return Minecraft.getInstance().level!!.registryAccess()
        }

        if (RadGyms.implementation.environment() == Environment.SERVER) {
            if (server() != null) return server()!!.registryAccess()
        }

        return RegistryAccess.EMPTY
    }
}
