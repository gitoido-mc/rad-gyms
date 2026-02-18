/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.entity.Trainer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.AttributeSupplier

private typealias ETRegistry = Registry<EntityType<*>>
private typealias ETRegistryKey = ResourceKey<Registry<EntityType<*>>>

object RadGymsEntities : PlatformRegistry<ETRegistry, ETRegistryKey, EntityType<*>>() {
    override val registry: ETRegistry = BuiltInRegistries.ENTITY_TYPE
    override val resourceKey: ETRegistryKey = Registries.ENTITY_TYPE

    @JvmField
    val GYM_TRAINER: EntityType<Trainer> = this.create(
        RadGyms.modId("gym_trainer"),
        EntityType.Builder.of(::Trainer, MobCategory.CREATURE).build(RadGyms.modId("gym_trainer").toString())
    )

    fun registerAttributes(consumer: (EntityType<out LivingEntity>, AttributeSupplier.Builder) -> Unit) {
        consumer(GYM_TRAINER, Trainer.createAttributes())
    }
}
