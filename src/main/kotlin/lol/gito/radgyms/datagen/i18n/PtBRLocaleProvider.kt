package lol.gito.radgyms.datagen.i18n

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.item.ItemGroupManager
import lol.gito.radgyms.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class PtBRLocaleProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricLanguageProvider(
    dataOutput,
    "pt_br",
    registryLookup
) {
    override fun generateTranslations(
        wrapperLookup: RegistryWrapper.WrapperLookup,
        translationBuilder: TranslationBuilder
    ) {
        translationBuilder.add(ItemGroupManager.GYMS_GROUP.displayName.string, "Ginásios Radicais")
        translationBuilder.add(ItemRegistry.GYM_KEY, "Chave do Ginásio")
        translationBuilder.add(BlockRegistry.GYM_ENTRANCE, "Entrada do Ginásio")
        translationBuilder.add(BlockRegistry.GYM_EXIT, "Saída do Ginásio")
        translationBuilder.add(
            RadGyms.modIdentifier("gui.common.set-gym-level"),
            "Selecione o nível desejado do ginásio"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("gui.common.leave-gym"),
            "Quer sair? As recompensas serão perdidas se o líder não for derrotado."
        )
        translationBuilder.add(RadGyms.modIdentifier("gui.common.increase"), "+1")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.increase-ten"), "+10")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.decrease"), "-1")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.decrease-ten"), "-10")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.ok"), "Sair do Ginásio")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.start"), "Iniciar Ginásio")
        translationBuilder.add(RadGyms.modIdentifier("gui.common.cancel"), "Cancelar")
        translationBuilder.add(RadGyms.modIdentifier("npc.trainer_junior"), "Treinador Júnior do Ginásio")
        translationBuilder.add(RadGyms.modIdentifier("npc.trainer_senior"), "Treinador Sênior do Ginásio")
        translationBuilder.add(RadGyms.modIdentifier("npc.leader"), "Líder de Ginásio")
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.key.not-in-main-hand"),
            "A chave do ginásio deve estar na sua mão principal!"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.common.no-response"),
            "Não foi possível obter resposta do servidor, tente novamente"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.error.gym_entrance.not-sneaking"),
            "Você precisa se agachar para quebrar a entrada do ginásio"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.gym_entrance_breaking"),
            "Entradas de ginásio não dropam ao serem quebradas. Se você quebrá-la, todos os jogadores perderão o acesso a essa entrada"
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.trainer_required"),
            "Derrote %s antes de me desafiar."
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.trainer_defeated"),
            "Você venceu! Vá desafiar o próximo treinador."
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.leader_defeated"),
            "Você venceu! Agora pode sair."
        )
        translationBuilder.add(
            RadGyms.modIdentifier("message.info.battle_fled"),
            "Poderes misteriosos estão te teleportando para longe do julgamento."
        )
    }
}