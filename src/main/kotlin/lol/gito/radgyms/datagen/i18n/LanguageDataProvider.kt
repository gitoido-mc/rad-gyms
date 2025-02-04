package lol.gito.radgyms.datagen.i18n

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.item.ItemGroupManager
import lol.gito.radgyms.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class LanguageDataProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricLanguageProvider(
    dataOutput,
    registryLookup
) {
    override fun generateTranslations(
        wrapperLookup: RegistryWrapper.WrapperLookup,
        translationBuilder: TranslationBuilder
    ) {
        translationBuilder.add(ItemGroupManager.GYMS_GROUP.displayName.string, "Radical Gyms")
        translationBuilder.add(ItemRegistry.GYM_KEY, "Gym key")
        translationBuilder.add(BlockRegistry.GYM_ENTRANCE, "Gym entrance")
        translationBuilder.add(BlockRegistry.GYM_EXIT, "Gym exit")
        translationBuilder.add(
            RadGyms.modIdentifier("gui.common.set-gym-level"),
            "Select desirable gym level"
        )
        translationBuilder.add(RadGyms.modIdentifier("gui.common.increase"), "+1")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.increase-ten"), "+10")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.decrease"), "-1")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.decrease-ten"), "-10")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.start"), "Start gym")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.cancel"), "Cancel")
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.key.not-in-main-hand"),
            "Gym key must be in your main hand!"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.common.no-response"),
            "Cannot acquire server response, try again"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.gym_entrance.not-sneaking"),
            "You need to sneak to break gym entrance"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.gym_entrance_breaking"),
            "Gym entrances do not drop when broken. If you break it, all players will lose access to this entrance"
        )
    }
}