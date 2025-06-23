package lol.gito.radgyms.block.entity

import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object BlockEntityRegistry {
    val GYM_ENTRANCE_ENTITY = registerBlockEntity(
        "gym_entrance_entity",
        ::GymEntranceEntity,
        BlockRegistry.GYM_ENTRANCE
    )

    val GYM_EXIT_ENTITY = registerBlockEntity(
        "gym_exit_entity",
        ::GymExitEntity,
        BlockRegistry.GYM_EXIT
    )

    private fun <F : BlockEntity> registerBlockEntity(
        name: String,
        entityFactory: BlockEntityFactory<out F>,
        vararg blocks: Block
    ): BlockEntityType<F> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        modId(name),
        BlockEntityType.Builder.create(entityFactory, *blocks).build()
    )

    fun register() {
        debug("Registering block entities")
    }
}
