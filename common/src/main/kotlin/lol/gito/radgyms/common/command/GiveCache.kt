package lol.gito.radgyms.common.command

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import lol.gito.radgyms.common.api.command.CommandInterface
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.cache.CacheHandler
import lol.gito.radgyms.common.command.argument.ElementalTypeArgumentType
import lol.gito.radgyms.common.command.argument.RarityArgumentType
import lol.gito.radgyms.common.helper.root
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Rarity

object GiveCache : CommandInterface {
    private const val NAME = "give"
    private const val SUB = "cache"
    private const val TYPE = "type"
    private const val RARITY = "rarity"
    private const val BOOST = "boost"
    private const val PLAYER = "player"

    @Suppress("LongMethod")
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val selfCommand = root(NAME)
            .requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                literal(SUB).then(
                    argument(TYPE, ElementalTypeArgumentType.type()).then(
                        argument(RARITY, RarityArgumentType.rarity())
                            .executes {
                                execute(
                                    it,
                                    it.source.playerOrException,
                                    ElementalTypeArgumentType.getType(it, TYPE),
                                    RarityArgumentType.getRarity(it, RARITY)
                                )
                            }
                    )
                )
            )

        val selfBoostCommand = root(NAME)
            .requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                literal(SUB).then(
                    argument(TYPE, ElementalTypeArgumentType.type()).then(
                        argument(RARITY, RarityArgumentType.rarity()).then(
                            argument(
                                BOOST,
                                IntegerArgumentType.integer(1, Cobblemon.config.shinyRate.toInt())
                            ).executes {
                                execute(
                                    it,
                                    it.source.playerOrException,
                                    ElementalTypeArgumentType.getType(it, TYPE),
                                    RarityArgumentType.getRarity(it, RARITY),
                                    IntegerArgumentType.getInteger(it, BOOST)
                                )
                            }
                        )
                    )
                )
            )

        val otherCommand = root(NAME)
            .requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                literal(SUB).then(
                    argument(TYPE, ElementalTypeArgumentType.type()).then(
                        argument(RARITY, RarityArgumentType.rarity()).then(
                            argument(PLAYER, EntityArgument.player()).executes {
                                execute(
                                    it,
                                    it.player(),
                                    ElementalTypeArgumentType.getType(it, TYPE),
                                    RarityArgumentType.getRarity(it, RARITY),
                                    IntegerArgumentType.getInteger(it, BOOST)
                                )
                            }
                        )
                    )
                )
            )


        val otherBoostCommand = root(NAME)
            .requires { it.hasPermission(Commands.LEVEL_GAMEMASTERS) }.then(
                literal(SUB).then(
                    argument(TYPE, ElementalTypeArgumentType.type()).then(
                        argument(RARITY, RarityArgumentType.rarity()).then(
                            argument(
                                BOOST,
                                IntegerArgumentType.integer(1, Cobblemon.config.shinyRate.toInt())
                            ).then(
                                argument(PLAYER, EntityArgument.player()).executes {
                                    execute(
                                        it,
                                        it.player(),
                                        ElementalTypeArgumentType.getType(it, TYPE),
                                        RarityArgumentType.getRarity(it, RARITY),
                                        IntegerArgumentType.getInteger(it, BOOST)
                                    )
                                }
                            )
                        )
                    )
                )
            )


        dispatcher.register(selfCommand)
        dispatcher.register(selfBoostCommand)
        dispatcher.register(otherCommand)
        dispatcher.register(otherBoostCommand)
    }

    override fun execute(context: CommandContext<CommandSourceStack>): Int = 0

    fun execute(
        context: CommandContext<CommandSourceStack>,
        player: ServerPlayer,
        type: ElementalType,
        rarity: Rarity,
        boost: Int = 0
    ): Int {
        try {

            val poke: Pokemon = CacheHandler.getPoke(
                type,
                rarity,
                player,
                boost
            )

            CACHE_ROLL_POKE.emit(
                GymEvents.CacheRollPokeEvent(
                    player,
                    poke,
                    type.showdownId.lowercase(),
                    rarity,
                    boost
                )
            )

        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            context.player().sendSystemMessage(
                Component.literal("Cannot generate $rarity $type poke, caught error: ${e.message}")
            )
            return -1
        }


        return 1
    }
}
