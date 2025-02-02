package lol.gito.radgyms.item

import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import lol.gito.radgyms.RadGyms.modIdentifier

object ItemGroupManager {
    val GYMS_GROUP: OwoItemGroup = OwoItemGroup
        .builder(modIdentifier("items")) { Icon.of(ItemRegistry.GYM_KEY) }
        .disableDynamicTitle()
        .build()

    fun register() {
        GYMS_GROUP.initialize()
    }
}