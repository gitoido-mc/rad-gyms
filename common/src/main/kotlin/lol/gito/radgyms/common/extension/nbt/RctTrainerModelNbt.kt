package lol.gito.radgyms.common.extension.nbt

import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import net.minecraft.nbt.CompoundTag


fun CompoundTag.getRctTrainerModel(key: String): TrainerModel? {
    val tag = this.getCompound(key) ?: return null

    return TrainerModel(
        tag.getString("Name"),
        JTO.of { RCTBattleAI() },
        tag.getRadGymsTrainerBag("Bag") ?: mutableListOf(),
        tag.getRadGymsTrainerTeam("Team") ?: mutableListOf(),
    )
}

fun CompoundTag.putRctTrainerModel(key: String, value: TrainerModel) {
    val tag = CompoundTag()

    tag.putString("Name", value.name.toString())
    tag.putRadGymsTrainerBag("Bag", value.bag)
    tag.putRadGymsTrainerTeam("Team", value.team)

    this.put(key, tag)
}
