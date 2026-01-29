/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.world.item.Rarity

object RaritySerializer : KSerializer<Rarity> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "net.minecraft.world.item.Rarity",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: Rarity
    ) = encoder.encodeString(value.serializedName)

    override fun deserialize(decoder: Decoder): Rarity =
        Rarity.valueOf(decoder.decodeString())
}