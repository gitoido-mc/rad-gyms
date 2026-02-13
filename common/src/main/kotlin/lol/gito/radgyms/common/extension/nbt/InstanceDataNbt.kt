package lol.gito.radgyms.common.extension.nbt

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.gym.GymTemplate
import lol.gito.radgyms.common.registry.RadGymsTemplates
import net.minecraft.nbt.CompoundTag
import java.util.UUID

fun CompoundTag.getRadGymsInstanceData(key: String): Gym? {
    val tag = this.getCompound(key) ?: return null
    val template = RadGymsTemplates.templates[tag.getString("Template")]!!
    val player = RadGyms.implementation.server()!!.playerList.getPlayer(
        UUID.fromString(key)
    )

    return Gym(
        template = GymTemplate.fromDto(
            player,
            template,
            tag.getInt("Level"),
            tag.getString("Type")
        ),
        npcList = emptyMap(),
        coords = tag.getBlockPos("Coords")!!,
        level = tag.getInt("Level"),
        type = tag.getString("Type"),
        label = tag.getString("Label")
    )
}

fun CompoundTag.putRadGymsInstanceData(key: String, value: Gym) {
    val nbt = CompoundTag()
    val trainersNbt = CompoundTag()

    val templateKey = RadGymsTemplates.templates.firstNotNullOf { (key, template) ->
        if (value.template.id == template.id) return@firstNotNullOf key
        return@firstNotNullOf null
    }


    nbt.putString("Template", templateKey)
    nbt.putBlockPos("Coords", value.coords)
    nbt.putInt("Level", value.level)
    nbt.putString("Type", value.type)
    nbt.putString("Label", value.label)

    value.npcList.forEach { (uuid, model) ->
        trainersNbt.putRadGymsTrainerModel(uuid.toString(), model)
    }

    nbt.put("Trainers", trainersNbt)

    this.put(key, nbt)
}
