/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.entity

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object EntityManager {
    val GYM_TRAINER: EntityType<Trainer> = Registry.register(
        Registries.ENTITY_TYPE,
        modId("gym_trainer"),
        EntityType.Builder.create(::Trainer, SpawnGroup.CREATURE).build(modId("gym_trainer").toString())
    )

    fun register() {
        debug("Registering mod entities")
        FabricDefaultAttributeRegistry.register(GYM_TRAINER, Trainer.createAttributes())
    }
}
