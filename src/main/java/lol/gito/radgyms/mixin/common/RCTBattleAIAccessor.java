/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.common;

import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RCTBattleAI.class)
public interface RCTBattleAIAccessor {
    @Accessor("moveBias")
    double getMoveBias();

    @Accessor("statusMoveBias")
    double getStatusMoveBias();

    @Accessor("switchBias")
    double getSwitchBias();

    @Accessor("itemBias")
    double getItemBias();

    @Accessor("maxSelectMargin")
    double getMaxSelectMargin();
}
