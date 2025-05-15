package lol.gito.radgyms.block.entity

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGyms.debug
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import software.bernie.geckolib.util.RenderUtil

class GymEntranceEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(BlockEntityRegistry.GYM_ENTRANCE_ENTITY, pos, state), GeoBlockEntity {
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
            .getOrDefault(player.uuid.toString(), CONFIG.maxEntranceUses)
            .coerceIn(0, CONFIG.maxEntranceUses)
        val maxUses = CONFIG.maxEntranceUses - playerCounter
        debug("Uses left for $player for $pos gym entrance: $maxUses (config max: ${CONFIG.maxEntranceUses})")
        return playerCounter
    }

    // Geckolib
    private val deployAnim = RawAnimation
        .begin()
        .thenPlay("animation.gym_entrance.bob1")
        .thenLoop("animation.gym_entrance.idle")

    private val animationCache = GeckoLibUtil.createInstanceCache(this)

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = this.animationCache

    override fun getTick(blockEntity: Any): Double {
        return RenderUtil.getCurrentTick()
    }

    override fun registerControllers(controllers: ControllerRegistrar) {
        val defaultAnimController = AnimationController(
            this,
            "gym_entrance",
            ::variadicAnimController
        )
        defaultAnimController.triggerableAnim(
            "open",
            RawAnimation.begin().thenPlay("animation.gym_entrance.open").thenLoop("animation.gym_entrance.open_idle")
        )
        defaultAnimController.triggerableAnim(
            "close",
            RawAnimation.begin().thenPlay("animation.gym_entrance.shut").thenLoop("animation.gym_entrance.idle")
        )
        defaultAnimController.triggerableAnim(
            "bob1",
            RawAnimation.begin().thenPlay("animation.gym_entrance.bob1")
        )
        defaultAnimController.triggerableAnim(
            "bob2",
            RawAnimation.begin().thenPlay("animation.gym_entrance.bob2")
        )
        defaultAnimController.triggerableAnim(
            "bob3",
            RawAnimation.begin().thenPlay("animation.gym_entrance.bob3")
        )
        defaultAnimController.triggerableAnim(
            "bob4",
            RawAnimation.begin().thenPlay("animation.gym_entrance.bob4")
        )
        controllers.add(defaultAnimController)
    }

    private fun variadicAnimController(state: AnimationState<GymEntranceEntity>): PlayState {
        return state.setAndContinue(deployAnim)
    }
}
