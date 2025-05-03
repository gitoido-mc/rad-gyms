package lol.gito.radgyms.network

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.cache.CacheHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.literal
import net.minecraft.util.Rarity

object CacheOpenPacketHandler {
    operator fun invoke(player: ServerPlayerEntity, type: ElementalType, rarity: Rarity, shinyBoost: Int) {
        val poke: Pokemon = CacheHandler.getPoke(type, rarity, player, shinyBoost, addToParty = true)
        player.sendMessage(literal("Rolled $rarity $type ${poke.species.name} shiny: ${poke.shiny}"))
        RadGyms.LOGGER.info("shiny boost: $shinyBoost")
        player.mainHandStack.decrementUnlessCreative(1, player)
    }
}
