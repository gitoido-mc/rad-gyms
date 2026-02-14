/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias ElementalListType = List<@Serializable(ElementalTypeSerializer::class) ElementalType>

object ElementalTypeSerializer : KSerializer<ElementalType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "com.cobblemon.mod.common.api.types.ElementalType",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: ElementalType
    ) = encoder.encodeString(value.showdownId)

    override fun deserialize(decoder: Decoder): ElementalType =
        ElementalTypes.get(decoder.decodeString()) ?: ElementalTypes.BUG
}
