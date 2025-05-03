package lol.gito.radgyms.jei

import lol.gito.radgyms.RadGyms
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import net.minecraft.util.Identifier

@JeiPlugin
object IntegrationPlugin : IModPlugin {
    override fun getPluginUid(): Identifier = RadGyms.modId("jei-plugin")


}
