package lol.gito.radgyms.block

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.Rarity

class EpicShardBlock : PokeShardBlockBase(rarity = Rarity.EPIC)

class RareShardBlock : PokeShardBlockBase(rarity = Rarity.RARE)

class UncommonShardBlock : PokeShardBlockBase(rarity = Rarity.UNCOMMON)

class CommonShardBlock : PokeShardBlockBase(rarity = Rarity.COMMON)

open class PokeShardBlockBase(val rarity: Rarity) : Block(Settings.copy(Blocks.LAPIS_BLOCK)) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL
}
