package lol.gito.radgyms.block

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.runOnServer
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import lol.gito.radgyms.gui.GuiHandler
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class GymEntranceBlock(settings: Settings) : BlockWithEntity(settings) {
    private val randomBobAnim = arrayOf("bob1", "bob2", "bob3", "bob4")

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymEntranceBlock(settings) }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)

        if (!world.isClient) {
            (world as ServerWorld).chunkManager.markForUpdate(pos)
        }
    }

    override fun hasRandomTicks(state: BlockState): Boolean = true

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.randomTick(state, world, pos, random)
        val blockEntity = world.getBlockEntity(pos)!! as GymEntranceEntity
        if (world.isClient && random.nextBetween(0, 1) > 0.5f) return
        blockEntity.triggerAnim("gym_entrance", randomBobAnim.random())
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.getBlockEntity(pos) !is GymEntranceEntity) return super.onUse(state, world, pos, player, hit)
        val gymEntrance: GymEntranceEntity = world.getBlockEntity(pos) as GymEntranceEntity

        // fainted party check
        if (!world.isClient) {
            val party = Cobblemon.implementation.server()!!.playerManager.getPlayer(player.uuid)!!.party()
            if (party.all { it.isFainted() }) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))
                debug("Player ${player.uuid} tried to use $pos gym entry with party fainted, denying...")
                gymEntrance.triggerAnim("gym_entrance", randomBobAnim.random())
                return ActionResult.PASS
            }
        }


        if (player.mainHandStack.isOf(Items.DEBUG_STICK)) {
            gymEntrance.resetPlayerUseCounter()
            gymEntrance.triggerAnim("gym_entrance", randomBobAnim.random())
            return ActionResult.SUCCESS
        }

        if (gymEntrance.usesLeftForPlayer(player) == 0) {
            if (!world.isClient) {
                player.sendMessage(
                    translatable(modId("message.info.gym_entrance_exhausted").toTranslationKey())
                )
            }
            debug("Player ${player.uuid} tried to use $pos gym entry with tries exhausted, denying...")
            gymEntrance.triggerAnim("gym_entrance", randomBobAnim.random())
            return ActionResult.PASS
        }

        if (world.isClient && !player.mainHandStack.isOf(Items.DEBUG_STICK)) {
            debug(
                "Client: Opening gym entry screen for player ${player.uuid} (tries left: ${
                    gymEntrance.usesLeftForPlayer(
                        player
                    )
                })"
            )
            GuiHandler.openGymEntranceScreen(player, gymEntrance.gymType, pos, gymEntrance.usesLeftForPlayer(player))
        }


        return ActionResult.SUCCESS
    }
}
