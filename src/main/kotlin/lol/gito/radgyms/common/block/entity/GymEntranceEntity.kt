/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block.entity

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.registry.RadGymsBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GymEntranceEntity(
    val pos: BlockPos,
    state: BlockState
) : BlockEntity(
    RadGymsBlockEntities.GYM_ENTRANCE_ENTITY,
    pos,
    state
) {
    private val playerUsageDataKey = "playerEntries"
    private val gymTypeKey = "type"
    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()
    var gymType: String = ElementalTypes.all().random().showdownId

    fun incrementPlayerUseCount(player: Player) {
        playerUseCounter[player.uuid.toString()] = playerUseCounter
            .getOrDefault(player.uuid.toString(), 0)
            .inc()
            .coerceIn(0, CONFIG.maxEntranceUses!!)

        setChanged()
        debug(
            "Increased player ${player.uuid} tries (${playerUseCounter[player.uuid.toString()]}) for $pos gym entrance"
        )
    }

//    fun resetPlayerUseCounter() {
//        playerUseCounter.clear()
//        setChanged()
//        debug("Reset usage count for $pos gym entrance")
//    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registryLookup: HolderLookup.Provider): CompoundTag =
        saveWithoutMetadata(registryLookup)

    override fun saveAdditional(nbt: CompoundTag, registryLookup: HolderLookup.Provider) {
        val playerEntries = nbt.getCompound(playerUsageDataKey)
        for ((key, value) in playerUseCounter) {
            playerEntries.putInt(key, value)
            debug("Writing player entry data ($key : $value) for $pos gym entrance")
        }
        nbt.put(playerUsageDataKey, playerEntries)
        nbt.putString(gymTypeKey, gymType)
        debug("Writing $gymTypeKey $gymType gym props for $pos gym entrance")
        super.saveAdditional(nbt, registryLookup)
    }

    override fun loadAdditional(nbt: CompoundTag, registryLookup: HolderLookup.Provider) {
        super.loadAdditional(nbt, registryLookup)

        gymType = nbt.getString(gymTypeKey)
        debug("Wrote $gymType type for $pos gym entrance block entity")

        val playerEntriesNBT = nbt.getCompound(playerUsageDataKey)
        for (key in playerEntriesNBT.allKeys) {
            playerUseCounter[key] = playerEntriesNBT.getInt(key)
            debug("Wrote player entry data ($key:${playerUseCounter[key]}) for $pos gym entrance block entity")
        }
    }

    fun usesLeftForPlayer(player: Player): Int {
        val playerCounter = playerUseCounter
            .getOrDefault(player.uuid.toString(), 0)
            .coerceIn(0, CONFIG.maxEntranceUses!!)

        debug("Uses left for $player for $pos gym entrance: ${CONFIG.maxEntranceUses!! - playerCounter} (config max: ${CONFIG.maxEntranceUses!!})")
        return CONFIG.maxEntranceUses!! - playerCounter
    }
}
