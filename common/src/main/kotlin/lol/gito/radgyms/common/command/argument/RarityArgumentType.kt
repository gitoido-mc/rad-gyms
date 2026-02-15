package lol.gito.radgyms.common.command.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lol.gito.radgyms.common.helper.tl
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.world.item.Rarity
import java.util.concurrent.CompletableFuture

class RarityArgumentType : ArgumentType<Rarity> {
    override fun parse(reader: StringReader): Rarity = try {
        return Rarity.valueOf(reader.readString())
    } catch (_: CommandSyntaxException) {
        throw SimpleCommandExceptionType(INVALID_RARITY).createWithContext(reader)
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(
            Rarity.entries.map { it.name.lowercase() },
            builder
        )
    }

    override fun getExamples(): Collection<String> = Rarity.entries.map { it.name.lowercase() }

    companion object {
        val INVALID_RARITY = tl("message.error.argument.invalid_rarity")

        fun rarity() = RarityArgumentType()

        fun <S> getRarity(context: CommandContext<S>, name: String): Rarity =
            context.getArgument(name, Rarity::class.java)
    }
}
