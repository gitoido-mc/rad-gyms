package lol.gito.radgyms.common.rct.converter

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.abilities.Abilities
import com.cobblemon.mod.common.api.moves.Moves.getByName
import com.cobblemon.mod.common.api.pokemon.Natures.getNature
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies.getByIdentifier
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.tera.TeraType
import com.cobblemon.mod.common.api.types.tera.TeraTypes
import com.cobblemon.mod.common.pokemon.EVs
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.util.cobblemonResource
import com.gitlab.srcmc.rctapi.api.errors.RCTError
import com.gitlab.srcmc.rctapi.api.errors.RCTErrors
import com.gitlab.srcmc.rctapi.api.errors.RCTException
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.models.converter.Converter
import com.gitlab.srcmc.rctapi.api.util.Locations
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

object PokemonPropertiesConverter : Converter<PokemonModel, PokemonProperties> {
    private const val MOVESET_SIZE: Int = 4

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    override fun toTarget(
        model: PokemonModel,
        errors: RCTErrors<RCTException>
    ): PokemonProperties {
        val props = PokemonProperties()
        if (!model.species.isBlank()) {
            val species = getByIdentifier(ResourceLocation.parse(model.species.lowercase()))
            if (species != null) {
                props.species = species.resourceIdentifier.path
            } else {
                errors.add(RCTError.of("wrong species identifier ${model.species.lowercase()}"))
            }
        }

        if (model.nickname.literal != null) {
            props.nickname = model.nickname.getComponent()
        }

        props.gender = when (model.gender) {
            Gender.FEMALE.name -> Gender.FEMALE
            Gender.MALE.name -> Gender.MALE
            else -> Gender.GENDERLESS
        }

        props.level = model.level.coerceIn(0, Cobblemon.config.maxPokemonLevel)

        if (!model.nature.isBlank()) {
            if (getNature(ResourceLocation.parse(model.nature)) != null) {
                props.nature = ResourceLocation.parse(model.nature).path
            } else {
                errors.add(RCTError.of("wrong nature identifier ${model.ability}"))
            }
        }

        if (!model.ability.isBlank()) {
            val ability = Abilities.get(model.ability)
            if (ability != null) {
                props.ability = ability.name
            } else {
                errors.add(RCTError.of("wrong ability identifier ${model.ability}"))
            }
        }

        if (model.moveset.size > MOVESET_SIZE) {
            errors.add(RCTError.of("too many moves ${model.moveset.size}/4"))
        }

        val moves = mutableListOf<String>()
        model.moveset.toList().take(MOVESET_SIZE).forEach { m: String? ->
            val move = getByName(Locations.withoutNamespace(m!!))
            if (move != null) {
                moves.add(move.name)
            } else {
                errors.add(RCTError.of("invalid move: $m"))
            }

        }
        props.moves = moves

        props.ivs = Cobblemon.statProvider.createEmptyIVs(0).apply {
            set(Stats.HP, model.iVs.hp.coerceIn(0, IVs.MAX_VALUE))
            set(Stats.ATTACK, model.iVs.atk.coerceIn(0, IVs.MAX_VALUE))
            set(Stats.DEFENCE, model.iVs.def.coerceIn(0,IVs.MAX_VALUE ))
            set(Stats.SPECIAL_ATTACK, model.iVs.spA.coerceIn(0, IVs.MAX_VALUE))
            set(Stats.SPECIAL_DEFENCE, model.iVs.spD.coerceIn(0, IVs.MAX_VALUE))
            set(Stats.SPEED, model.iVs.spe.coerceIn(0, IVs.MAX_VALUE))
        }

        props.evs = Cobblemon.statProvider.createEmptyEVs().apply {
            set(Stats.HP, model.eVs.hp.coerceIn(0, EVs.MAX_STAT_VALUE))
            set(Stats.ATTACK, model.eVs.atk.coerceIn(0, EVs.MAX_STAT_VALUE))
            set(Stats.DEFENCE, model.eVs.def.coerceIn(0, EVs.MAX_STAT_VALUE))
            set(Stats.SPECIAL_ATTACK, model.eVs.spA.coerceIn(0, EVs.MAX_STAT_VALUE))
            set(Stats.SPECIAL_DEFENCE, model.eVs.spD.coerceIn(0, EVs.MAX_STAT_VALUE))
            set(Stats.SPEED, model.eVs.spe.coerceIn(0, EVs.MAX_STAT_VALUE))
        }

        props.shiny = model.isShiny
        props.aspects = model.aspects
        props.gmaxFactor = model.gimmicks.gmax()

        if (model.gimmicks.tera() != null) {
            val tt: TeraType? = TeraTypes.get(model.gimmicks.tera())
            if (tt != null) {
                props.teraType = tt.name
            } else {
                errors.add(RCTError.of("invalid tera type '${model.gimmicks.tera()}'"))
            }
        }

        var item: String? = null
        var itemValid = false

        for (itemId in model.heldItems) {
            item = Locations.withNamespace("cobblemon", itemId)
            val rl = ResourceLocation.parse(item)
            if (BuiltInRegistries.ITEM.containsKey(rl)) {
                val stack = BuiltInRegistries.ITEM.get(rl).defaultInstance
                if (!stack.isEmpty) {
                    props.heldItem = rl.toString()
                    itemValid = true
                } else {
                    item = null
                }
                break
            }
        }

        if (item != null && !itemValid) {
            errors.add(RCTError.of("invalid held item '$item'"))
        }

        return props
    }
}
