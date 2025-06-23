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