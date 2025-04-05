package lol.gito.radgyms.datagen.i18n

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.item.group.ItemGroupManager
import lol.gito.radgyms.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class EnUSLocaleProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricLanguageProvider(
    dataOutput,
    "en_us",
    registryLookup
) {

    private fun provideTranslations(): MutableMap<String, String> {
        return mutableMapOf(
            ItemGroupManager.GYMS_GROUP.displayName.string to
                    "Rad Gyms",
            ItemRegistry.EXIT_ROPE.translationKey to
                    "Exit rope",
            ItemRegistry.EXIT_ROPE.translationKey.plus(".tooltip") to
                    "Single-use rope to escape the gym trial",
            ItemRegistry.EXIT_ROPE.translationKey.plus(".failed") to
                    "It cannot be used here",
            ItemRegistry.GYM_KEY.translationKey to
                    "Gym key",
            ItemRegistry.GYM_KEY.translationKey.plus(".attuned") to
                    "Attuned to %s",
            BlockRegistry.GYM_ENTRANCE.translationKey to
                    "Gym Entrance",
            BlockRegistry.GYM_EXIT.translationKey to
                    "Gym Exit",
            modId("type").withSuffixedPath(".chaos").toTranslationKey() to
                    "Chaos",
            modId("gym_reward").toTranslationKey("item") to
                    "Level %s %s gym reward cache",
            modId("gui.common.set-gym-level").toTranslationKey() to
                    "Select desirable gym level",
            modId("gui.common.leave-gym").toTranslationKey() to
                    "You want to leave? Rewards will be lost if leader is not beaten.",
            modId("gui.common.increase").toTranslationKey() to
                    "+1",
            modId("gui.common.increase-ten").toTranslationKey() to
                    "+10",
            modId("gui.common.decrease").toTranslationKey() to
                    "-1",
            modId("gui.common.decrease-ten").toTranslationKey() to
                    "-10",
            modId("gui.common.leave").toTranslationKey() to
                    "Leave Gym",
            modId("gui.common.start").toTranslationKey() to
                    "Start Gym",
            modId("gui.common.cancel").toTranslationKey() to
                    "Cancel",
            modId("gui.common.uses_left").toTranslationKey() to
                    "Entrance uses left: %s",
            modId("npc.trainer_junior").toTranslationKey() to
                    "Junior gym trainer",
            modId("npc.trainer_senior").toTranslationKey() to
                    "Senior gym trainer",
            modId("npc.leader").toTranslationKey() to
                    "Gym Leader",
            modId("message.info.gym_entrance_breaking").toTranslationKey() to
                    "Gym entrances do not drop when broken. If you break it, all players will lose access to this entrance",
            modId("message.info.gym_entrance_exhausted").toTranslationKey() to
                    "This gym entrance lost all his energies, look for another one",
            modId("message.info.gym_entrance_party_fainted").toTranslationKey() to
                    "Your party requires healing",
            modId("message.info.trainer_required").toTranslationKey() to
                    "Go fight %s before challenging me.",
            modId("message.info.trainer_defeated").toTranslationKey() to
                    "You won! Go challenge next trainer.",
            modId("message.info.leader_defeated").toTranslationKey() to
                    "Congratulations on beating the gym!",
            modId("message.info.gym_failed").toTranslationKey() to
                    "Mysterious forces are teleporting you away from the trial",
            modId("message.info.gym_init").toTranslationKey() to
                    "Mysterious forces are teleporting you to %s trial",
            modId("message.info.gym_complete").toTranslationKey() to
                    "An exit appeared somewhere in gym",
            modId("message.error.common.no-response").toTranslationKey() to
                    "Cannot acquire server response, try again",
            modId("message.error.key.not-in-main-hand").toTranslationKey() to
                    "Gym key must be in your main hand",
            modId("message.error.gym_entrance.not-sneaking").toTranslationKey() to
                    "You need to sneak to break gym entrance",
        )
    }


    override fun generateTranslations(
        wrapperLookup: RegistryWrapper.WrapperLookup,
        translationBuilder: TranslationBuilder
    ) {
        for (item in provideTranslations()) {
            translationBuilder.add(item.key, item.value)
        }
    }
}
