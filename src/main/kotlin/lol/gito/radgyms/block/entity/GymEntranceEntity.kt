package lol.gito.radgyms.block.entity

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGymsConfig
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos

class GymEntranceEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(
    BlockEntityRegistry.GYM_ENTRANCE_ENTITY,
    pos,
    state
) {
    private val playerUsageDataKey = "playerEntries"
    private val gymTypeKey = "type"
    var gymType: String = ElementalTypes.all().random().name
    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()

    fun incrementPlayerUseCount(player: PlayerEntity) {
        val useCounter = playerUseCounter.getOrDefault(player.uuid.toString(), 0)

        playerUseCounter[player.uuid.toString()] = useCounter + 1
        markDirty()
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(registryLookup)

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        val playerEntries = nbt.getCompound(playerUsageDataKey)
        for ((key, value) in playerUseCounter) {
            playerEntries.putInt(key, value)
        }
        nbt.put(playerUsageDataKey, playerEntries)
        nbt.putString(gymTypeKey, gymType)
        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)

        gymType = nbt.getString(gymTypeKey)

        val playerEntriesNBT = nbt.getCompound(playerUsageDataKey)
        for (key in playerEntriesNBT.keys) {
            playerUseCounter[key] = playerEntriesNBT.getInt(key)
        }
    }

    fun usesLeftForPlayer(player: PlayerEntity): Int {
        val playerCounter = playerUseCounter
            .getOrDefault(player.uuid.toString(), 0)
            .coerceIn(0, CONFIG.maxEntranceUses)
        return CONFIG.maxEntranceUses - playerCounter
    }
}