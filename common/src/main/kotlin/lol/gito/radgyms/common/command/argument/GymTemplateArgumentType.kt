package lol.gito.radgyms.common.command.argument

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsTemplates
import net.minecraft.commands.SharedSuggestionProvider
import java.util.concurrent.CompletableFuture

class GymTemplateArgumentType : ArgumentType<GymJson> {
    override fun parse(reader: StringReader): GymJson {
        try {
            val templateToken = reader.readString()
            val template = RadGymsTemplates
                .templates
                .getOrDefault(templateToken, null)
                ?: throw NoSuchElementException()
            return template
        } catch (_: Exception) {
            throw SimpleCommandExceptionType(INVALID_TEMPLATE).createWithContext(reader)
        }
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(
            RadGymsTemplates.templates.map { it.key },
            builder
        )
    }

    override fun getExamples(): Collection<String> = listOf(
        ElementalTypes.WATER.name,
        ElementalTypes.FIRE.name,
        ElementalTypes.GRASS.name
    )

    companion object {
        val INVALID_TEMPLATE = tl("message.error.argument.invalid_gym_template")

        fun templates() = GymTemplateArgumentType()

        fun <S> getTemplate(context: CommandContext<S>, name: String): GymJson {
            return context.getArgument(name, GymJson::class.java)
        }
    }
}
