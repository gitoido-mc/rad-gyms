package lol.gito.radgyms.block.entity

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.state.GymEntranceState
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.state.property.Properties.HORIZONTAL_FACING
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import javax.swing.text.View

class GymEntranceEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(BlockEntityRegistry.GYM_ENTRANCE_ENTITY, pos, state) {
    private val playerUsageDataKey = "playerEntries"
    private val gymTypeKey = "type"
    private var playerUseCounter: MutableMap<String, Int> = mutableMapOf()
    var gymType: String = ElementalTypes.all().random().name
    val posableState: GymEntranceState = GymEntranceState()

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

    fun onViewerCountUpdate(world: World, pos: BlockPos, state: BlockState, oldViewerCount: Int, newViewerCount: Int) {
        val block = state.block
        world.addSyncedBlockEvent(pos, block, 1, newViewerCount)
    }


    private val stateManager: ViewerCountManager = object : ViewerCountManager() {
        override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {
            playSound(world, pos, state, CobblemonSounds.GILDED_CHEST_OPEN)
        }

        override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {
            playSound(world, pos, state, CobblemonSounds.GILDED_CHEST_OPEN)
        }

        override fun onViewerCountUpdate(
            world: World,
            pos: BlockPos,
            state: BlockState,
            oldViewerCount: Int,
            newViewerCount: Int
        ) {
            this@GymEntranceEntity.onViewerCountUpdate(world, pos, state, oldViewerCount, newViewerCount)
        }

        override fun isPlayerViewing(player: PlayerEntity): Boolean = true
    }

    companion object {
        fun playSound(world: World, pos: BlockPos, state: BlockState, sound: SoundEvent) {
            var d = pos.x.toDouble() + 0.5
            val e = pos.y.toDouble() + 0.5
            var f = pos.z.toDouble() + 0.5
            val direction = state.get(HORIZONTAL_FACING)
            d += direction.offsetX.toDouble() * 0.5
            f += direction.offsetZ.toDouble() * 0.5
            world.playSound(
                null,
                d,
                e,
                f,
                sound,
                SoundCategory.BLOCKS,
                0.5f,
                world.random.nextFloat() * 0.1f + 0.9f
            )
        }

    }
}
