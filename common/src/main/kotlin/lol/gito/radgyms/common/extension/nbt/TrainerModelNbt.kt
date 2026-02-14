package lol.gito.radgyms.common.extension.nbt

import lol.gito.radgyms.common.api.dto.trainer.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsTrainerModel(key: String): TrainerModel {
    val tag = this.getCompound(key)

    return TrainerModel(
        id = tag.getString("Id"),
        format = GymBattleFormat.valueOf(tag.getString("Format")),
        leader = tag.getBoolean("Leader"),
        requires = tag.getString("Requires"),
        npc = tag.getRadGymsTrainerEntityData("EntityData")!!,
        trainer = tag.getRctTrainerModel("Trainer")!!,
        battleRules = tag.getRadGymsBattleRules("BattleRules")!!
    )
}

fun CompoundTag.putRadGymsTrainerModel(key: String, value: TrainerModel) {
    val tag = CompoundTag()


    tag.putString("Id", value.id)
    tag.putString("Format", value.format.serializedName)
    tag.putBoolean("Leader", value.leader)
    if (value.requires != null) {
        tag.putString("Requires", value.requires)
    }

    tag.putRadGymsTrainerEntityData("EntityData", value.npc)
    tag.putRctTrainerModel("Trainer", value.trainer)
    tag.putRadGymsBattleRules("BattleRules", value.battleRules)

    this.put(key, tag)
}
