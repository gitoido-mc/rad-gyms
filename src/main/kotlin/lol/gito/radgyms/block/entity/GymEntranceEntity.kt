package lol.gito.radgyms.block.entity

import com.cobblemon.mod.common.api.types.ElementalTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import java.util.*

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

    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()
    var gymType: String = ElementalTypes.all().random().name

    fun incrementPlayerUseCount(player: PlayerEntity) {
        val useCounter = playerUseCounter.getOrDefault(player.uuid.toString(), 0)

        playerUseCounter[player.uuid.toString()] = useCounter + 1
        markDirty()
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        return createNbt(registryLookup)
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        for ((key, value) in playerUseCounter) {
            nbt.putInt(key, value)
        }

        nbt.putString(gymTypeKey, gymType)

        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)

        val data = nbt.getCompound(playerUsageDataKey)
        gymType = nbt.getString(gymTypeKey)

        for (key: String in nbt.keys) {
            try {
                UUID.fromString(key)
                playerUseCounter[key] = data.getInt(key)
            } catch (_: IllegalArgumentException) {}
        }
    }
}