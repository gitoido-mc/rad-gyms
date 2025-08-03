/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.block.entity

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.registry.BlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos

class GymEntranceEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(BlockEntityRegistry.GYM_ENTRANCE_ENTITY, pos, state) {
    private val playerUsageDataKey = "playerEntries"
    private val gymTypeKey = "type"
    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()
    var gymType: String = ElementalTypes.all().random().name

    fun incrementPlayerUseCount(player: PlayerEntity) {
        val useCounter = playerUseCounter.getOrDefault(player.uuid.toString(), 0)

        playerUseCounter[player.uuid.toString()] = useCounter + 1
        markDirty()
        debug(
            "Increased player ${player.uuid} tries (${playerUseCounter[player.uuid.toString()]}) for $pos gym entrance"
        )
    }

    fun resetPlayerUseCounter() {
        playerUseCounter.clear()
        markDirty()
        debug("Reset usage count for $pos gym entrance")
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound =
        createNbt(registryLookup)

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        val playerEntries = nbt.getCompound(playerUsageDataKey)
        for ((key, value) in playerUseCounter) {
            playerEntries.putInt(key, value)
            debug("Writing player entry data ($key : $value) for $pos gym entrance")
        }
        nbt.put(playerUsageDataKey, playerEntries)
        nbt.putString(gymTypeKey, gymType)
        debug("Writing $gymTypeKey $gymType gym props for $pos gym entrance")
        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)

        gymType = nbt.getString(gymTypeKey)
        debug("Wrote $gymType type for $pos gym entrance block entity")

        val playerEntriesNBT = nbt.getCompound(playerUsageDataKey)
        for (key in playerEntriesNBT.keys) {
            playerUseCounter[key] = playerEntriesNBT.getInt(key)
            debug("Wrote player entry data ($key:${playerUseCounter[key]}) for $pos gym entrance block entity")
        }
    }

    fun usesLeftForPlayer(player: PlayerEntity): Int {
        val playerCounter = playerUseCounter
            .getOrDefault(player.uuid.toString(), CONFIG.maxEntranceUses!!)
            .coerceIn(0, CONFIG.maxEntranceUses!!)

        debug("Uses left for $player for $pos gym entrance: ${CONFIG.maxEntranceUses!! - playerCounter} (config max: ${CONFIG.maxEntranceUses!!})")
        return playerCounter
    }
}
