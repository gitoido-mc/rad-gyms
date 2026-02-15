/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import lol.gito.radgyms.common.api.enumeration.GymReward

typealias GymRewardList = List<@Serializable(GymRewardSerializer::class) GymReward>

object GymRewardSerializer : KSerializer<GymReward> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "lol.gito.radgyms.common.api.enumeration.GymReward",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: GymReward
    ) = encoder.encodeString(value.serializedName)

    override fun deserialize(decoder: Decoder): GymReward =
        GymReward.valueOf(decoder.decodeString())
}
