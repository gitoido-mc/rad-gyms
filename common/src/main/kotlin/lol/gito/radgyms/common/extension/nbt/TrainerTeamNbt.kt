package lol.gito.radgyms.common.extension.nbt

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.gitlab.srcmc.rctapi.api.errors.RCTErrors
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.converter.PokemonPropertiesModelConverter
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsTrainerTeam(key: String): MutableList<PokemonModel> {
    val nbt = this.getCompound(key)

    return nbt.allKeys.map {
        PokemonModel(PokemonProperties.parse(nbt.getString(it)).create())
    }.toMutableList()
}

fun CompoundTag.putRadGymsTrainerTeam(key: String, value: MutableList<PokemonModel>) {
    val nbt = CompoundTag()

    val errors = RCTErrors.create()
    value.forEachIndexed { index, model ->
        val poke = PokemonPropertiesModelConverter().toTarget(model, errors)

        nbt.putString(index.toString(), poke.asString())
    }

    this.put(key, nbt)
}
