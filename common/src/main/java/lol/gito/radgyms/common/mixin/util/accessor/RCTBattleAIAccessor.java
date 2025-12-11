/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.util.accessor;

import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RCTBattleAI.class)
public interface RCTBattleAIAccessor {
    @Accessor(value = "moveBias", remap = false)
    double getMoveBias();

    @Accessor(value = "statusMoveBias", remap = false)
    double getStatusMoveBias();

    @Accessor(value = "switchBias", remap = false)
    double getSwitchBias();

    @Accessor(value = "itemBias", remap = false)
    double getItemBias();

    @Accessor(value = "maxSelectMargin", remap = false)
    double getMaxSelectMargin();
}
