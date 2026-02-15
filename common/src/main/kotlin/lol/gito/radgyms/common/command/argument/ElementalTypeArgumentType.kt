package lol.gito.radgyms.common.command.argument

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lol.gito.radgyms.common.helper.tl
import net.minecraft.commands.SharedSuggestionProvider
import java.util.concurrent.CompletableFuture

class ElementalTypeArgumentType : ArgumentType<ElementalType> {
    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): ElementalType {
        val cacheToken = reader.readString()
        return ElementalTypes.getOrException(cacheToken)
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(
            ElementalTypes.all().map { it.name.lowercase() },
            builder
        )
    }

    override fun getExamples(): Collection<String> = listOf(
        ElementalTypes.WATER.name,
        ElementalTypes.FIRE.name,
        ElementalTypes.GRASS.name
    )

    companion object {
        val INVALID_TYPE = tl("message.error.argument.invalid_elemental_type")

        fun type() = ElementalTypeArgumentType()

        fun <S> getType(context: CommandContext<S>, name: String): ElementalType {
            return context.getArgument(name, ElementalType::class.java)
        }
    }
}
