package lol.gito.radgyms.common.extension.nbt

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.dto.gym.Gym
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

    val npcList = mutableListOf<UUID>()

    tag.getCompound("Trainers").allKeys.forEach { index ->
        npcList.add(tag.getCompound("Trainers").getUUID(index))
    }

    return Gym(
        template = GymTemplate.fromDto(
            player,
            template,
            tag.getInt("Level"),
            tag.getString("Type")
        ),
        npcList = npcList,
        coords = tag.getBlockPos("Coords")!!,
        level = tag.getInt("Level"),
        type = tag.getString("Type")
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

    value.npcList.forEachIndexed { index, uuid ->
        trainersNbt.putUUID(index.toString(), uuid)
    }

    nbt.put("Trainers", trainersNbt)

    this.put(key, nbt)
}
