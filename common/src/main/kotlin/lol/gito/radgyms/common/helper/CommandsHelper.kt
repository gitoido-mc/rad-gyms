package lol.gito.radgyms.common.helper

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import lol.gito.radgyms.common.COMMANDS_PREFIX
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal

fun root(name: String): LiteralArgumentBuilder<CommandSourceStack> = literal(COMMANDS_PREFIX)
    .then(literal(name))
