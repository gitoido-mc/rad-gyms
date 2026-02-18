/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.helper

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import lol.gito.radgyms.common.COMMANDS_PREFIX
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal

fun root(name: String): LiteralArgumentBuilder<CommandSourceStack> = literal(COMMANDS_PREFIX)
    .then(literal(name))
