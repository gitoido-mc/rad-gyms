package lol.gito.radgyms.block.entity

import lol.gito.radgyms.RadGyms
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
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

    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()
    private lateinit var gymType: String

    fun incrementPlayerUseCount(player: PlayerEntity) {
        val useCounter = playerUseCounter.getOrDefault(player.uuid.toString(), 0)

        playerUseCounter[player.uuid.toString()] = useCounter + 1
        markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        val data = NbtCompound()

        for ((key, value) in playerUseCounter) {
            data.putInt(key, value)
        }

        nbt.putString(this.gymTypeKey, gymType)
        nbt.put(this.playerUsageDataKey, data)
        RadGyms.LOGGER.info(data.toString())

        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)

        val data = nbt.getCompound(playerUsageDataKey)
        gymType = data.getString(this.gymTypeKey)

        for (key: String in data.keys) {
            playerUseCounter[key] = data.getInt(key)
        }
    }
}