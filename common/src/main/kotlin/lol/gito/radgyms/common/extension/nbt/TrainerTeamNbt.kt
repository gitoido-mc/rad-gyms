package lol.gito.radgyms.common.extension.nbt

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.gitlab.srcmc.rctapi.api.errors.RCTErrors
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.converter.PokemonModelConverter
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsTrainerTeam(key: String): MutableList<PokemonModel>? {
    val nbt = this.getCompound(key) ?: return null

    return nbt.allKeys.map {
        PokemonModel(PokemonProperties.parse(nbt.getString(it)).create())
    }.toMutableList()
}

fun CompoundTag.putRadGymsTrainerTeam(key: String, value: MutableList<PokemonModel>) {
    val nbt = CompoundTag()

    value.forEachIndexed { index, model ->
        val errors = RCTErrors.create()
        val poke = PokemonModelConverter().toTarget(model, errors)

        nbt.putString(index.toString(), poke.createPokemonProperties(PokemonPropertyExtractor.ALL).asString())
    }

    this.put(key, nbt)
}
