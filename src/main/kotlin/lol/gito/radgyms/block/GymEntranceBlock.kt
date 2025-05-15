package lol.gito.radgyms.block

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.party
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import lol.gito.radgyms.gui.GuiHandler
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Properties.HORIZONTAL_FACING
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class GymEntranceBlock : HorizontalFacingBlock, BlockEntityProvider {
    constructor(settings: Settings) : super(settings) {
        defaultState = defaultState.with(HORIZONTAL_FACING, Direction.NORTH)
    }

    private val randomBobAnim = arrayOf("bob1", "bob2", "bob3", "bob4")

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED

    override fun hasRandomTicks(state: BlockState): Boolean = true

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun getCodec(): MapCodec<out HorizontalFacingBlock> =
        createCodec { settings: Settings -> GymEntranceBlock(settings) }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HORIZONTAL_FACING)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return VoxelShapes.fullCube()
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)!!.with(HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.randomTick(state, world, pos, random)
        val blockEntity = world.getBlockEntity(pos)!! as GymEntranceEntity
        @Suppress("detekt:MagicNumber")
        if (world.isClient && random.nextBetween(0, 10) > 5) return
        blockEntity.triggerAnim("gym_entrance", randomBobAnim.random())
    }

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
        if (validateParty(world, player, pos, gymEntrance)) return ActionResult.PASS

        if (validateMainHandStickPresence(player, gymEntrance)) return ActionResult.SUCCESS

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

    private fun validateMainHandStickPresence(
        player: PlayerEntity,
        gymEntrance: GymEntranceEntity
    ): Boolean {
        if (player.mainHandStack.isOf(Items.DEBUG_STICK)) {
            gymEntrance.resetPlayerUseCounter()
            gymEntrance.triggerAnim("gym_entrance", randomBobAnim.random())
            return true
        }
        return false
    }

    private fun validateParty(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        gymEntrance: GymEntranceEntity
    ): Boolean {
        if (!world.isClient) {
            val party = Cobblemon.implementation.server()!!.playerManager.getPlayer(player.uuid)!!.party()
            if (party.all { it.isFainted() }) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))
                debug("Player ${player.uuid} tried to use $pos gym entry with party fainted, denying...")
                gymEntrance.triggerAnim("gym_entrance", randomBobAnim.random())
                return true
            }
        }
        return false
    }
}
