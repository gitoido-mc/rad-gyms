/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.server.compat;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.battle.BattleManager;
import lol.gito.radgyms.RadGyms;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BattleManager.class)
public class OnRCTBattleManagerPokeClone {
    @Redirect(
        method = "toBattlePokemons(Z[Lcom/cobblemon/mod/common/pokemon/Pokemon;)Ljava/util/List;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;clone(ZLnet/minecraft/registry/DynamicRegistryManager;)Lcom/cobblemon/mod/common/pokemon/Pokemon;"
        )
    )
    private static Pokemon RadGyms$fixClone(Pokemon instance, boolean encoded, DynamicRegistryManager result) {
        RadGyms.INSTANCE.log("Ability: " +  instance.getAbility().getDisplayName());
        Pokemon pokemon = new Pokemon();
        RadGyms.INSTANCE.log(instance.getAbility().getDisplayName());
        Pokemon effected = pokemon.copyFrom(instance);
        RadGyms.INSTANCE.log("Effected Ability: " +  effected.getAbility().getDisplayName());

        assert instance.getAbility() == effected.getAbility();
        assert instance.getShiny() == effected.getShiny();
        assert instance.getAspects().equals(effected.getAspects());

        return effected;
    }
}
