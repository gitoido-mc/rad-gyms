package lol.gito.radgyms.entity

import lol.gito.radgyms.RadGyms
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object EntityManager {
    val GYM_TRAINER: EntityType<Trainer> = Registry.register(
        Registries.ENTITY_TYPE,
        RadGyms.modIdentifier("gym_trainer"),
        EntityType.Builder.create(::Trainer, SpawnGroup.CREATURE).build("gym_trainer")
    )

    fun register() {
        RadGyms.LOGGER.info("Registering entities")
        FabricDefaultAttributeRegistry.register(GYM_TRAINER, Trainer.createAttributes())
    }
}