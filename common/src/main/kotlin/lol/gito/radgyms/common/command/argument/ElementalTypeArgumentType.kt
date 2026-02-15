package lol.gito.radgyms.common.command.argument

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lol.gito.radgyms.common.helper.tl
import net.minecraft.commands.SharedSuggestionProvider
import java.util.concurrent.CompletableFuture

class ElementalTypeArgumentType : ArgumentType<ElementalType> {
    override fun parse(reader: StringReader): ElementalType {
        try {
            val cacheToken = reader.readString()
            return ElementalTypes.getOrException(cacheToken)
        } catch (_: Exception) {
            throw SimpleCommandExceptionType(INVALID_TYPE).createWithContext(reader)
        }
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(
            ElementalTypes.all().map { it.showdownId },
            builder
        )
    }

    override fun getExamples(): Collection<String> = listOf(
        ElementalTypes.WATER.showdownId,
        ElementalTypes.FIRE.showdownId,
        ElementalTypes.GRASS.showdownId
    )

    companion object {
        val INVALID_TYPE = tl("message.error.argument.invalid_elemental_type")

        fun type() = ElementalTypeArgumentType()

        fun <S> getType(context: CommandContext<S>, name: String): ElementalType =
            context.getArgument(name, ElementalType::class.java)
    }
}
