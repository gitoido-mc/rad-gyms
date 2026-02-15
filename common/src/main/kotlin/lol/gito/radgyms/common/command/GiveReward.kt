package lol.gito.radgyms.common.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.api.command.CommandInterface
import net.minecraft.commands.CommandSourceStack

object GiveReward : CommandInterface {
    private const val NAME = "give"
    private const val SUB = "reward"
    private const val TEMPLATE = "template"
    private const val LEVEL = "level"
    private const val TYPE = "type"
    private const val PLAYER = "player"

    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        TODO("Not yet implemented")
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int {

//
//    @Suppress("unused")
//    @Command("debug:reward")
//    @RequiresPermissionLevel(4)
//    fun debugReward(
//        context: CommandContext<CommandSourceStack>,
//        @Name("template") template: String,
//        @Name("level") @MinMax(min = "1", max = "100") level: Int,
//        @Name("type") type: String?
//    ): Int {
//        if (context.source.player != null) {
//            val gymDto = RadGyms.gymTemplateRegistry.getTemplateOrDefault(template)
//
//            if (gymDto == null) {
//                context.source.sendFailure(
//                    translatable(
//                        modId("message.error.command.debug_reward.no_template").toLanguageKey(), template
//                    )
//                )
//                return -1
//            }
//
//            level.apply {
//                this.coerceIn(1..100)
//            }
//
//            val gymType = when (type) {
//                null -> ElementalTypes.all().random().showdownId
//                else -> type
//            }
//
//            GENERATE_REWARD.emit(
//                GymEvents.GenerateRewardEvent(
//                    context.source.playerOrException,
//                    GymTemplate.fromDto(context.source.playerOrException, gymDto, level, type),
//                    level,
//                    gymType
//                )
//            )
//
//            context.source.sendSystemMessage(
//                translatable(
//                    modId("message.info.command.debug_reward").toLanguageKey(), template, translatable(
//                        cobblemonResource("type.${type}").toLanguageKey()
//                    ), level
//                )
//            )
//        } else {
//            context.source.sendFailure(
//                translatable(modId("message.error.command.debug_reward.no_player").toLanguageKey())
//            )
//            return -1
//        }
//    }
//
        return 1
    }
}
