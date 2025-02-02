package lol.gito.radgyms.block

import io.wispforest.owo.registration.reflect.BlockRegistryContainer
import lol.gito.radgyms.item.ItemGroupManager
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

class BlockRegistry: BlockRegistryContainer {
    companion object {
        @JvmField
        val GYM_ENTRANCE = GymEntranceBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK))

        @JvmField
        val GYM_EXIT = GymEntranceBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK))
    }

    override fun createBlockItem(block: Block, identifier: String): BlockItem {
        return BlockItem(block, Item.Settings().group(ItemGroupManager.GYMS_GROUP))
    }
}