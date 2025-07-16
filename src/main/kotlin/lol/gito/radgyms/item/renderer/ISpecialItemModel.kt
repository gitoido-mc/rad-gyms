/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.item.renderer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier
import java.util.function.Consumer
import java.util.stream.Stream

interface ISpecialItemModel {

    @Environment(EnvType.CLIENT)
    fun loadModels(unbakedModels: Stream<Identifier>, loader: Consumer<ModelIdentifier>)

    @Environment(EnvType.CLIENT)
    fun getRenderer(): SpecialItemRenderer

}
