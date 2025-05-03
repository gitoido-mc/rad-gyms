package lol.gito.radgyms.command

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.runOnServer
import com.mojang.brigadier.context.CommandContext
import de.maxhenkel.admiral.MinecraftAdmiral
import de.maxhenkel.admiral.annotations.Command
import de.maxhenkel.admiral.annotations.MinMax
import de.maxhenkel.admiral.annotations.Name
import de.maxhenkel.admiral.annotations.RequiresPermissionLevel
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.cache.CacheHandler
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.gym.GymTemplate
import lol.gito.radgyms.network.NetworkStackHandler
import lol.gito.radgyms.world.DimensionManager.RADGYMS_LEVEL_KEY
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.literal
import net.minecraft.text.Text.translatable
import net.minecraft.util.Rarity

@Command("radgyms")
object CommandRegistry {
    @Command("kick")
    @RequiresPermissionLevel(4)
    fun kick(
        context: CommandContext<ServerCommandSource>,
        @Name("player") player: ServerPlayerEntity
    ): Int {
        if (player.world.registryKey != RADGYMS_LEVEL_KEY) {
            if (context.source.isExecutedByPlayer) {
                context.source.sendError(
                    translatable(
                        modId("message.error.command.kick").toTranslationKey(),
                        context.source.player!!.name
                    )
                )
            }
            return -1
        }
        player.sendMessage(
            translatable(modId("message.info.command.op_kick").toTranslationKey())
        )
        CHANNEL.serverHandle(player).send(NetworkStackHandler.GymLeave(teleport = true))
        return 1
    }

    @Command("debug_reward")
    @RequiresPermissionLevel(4)
    fun debugReward(
        context: CommandContext<ServerCommandSource>,
        @Name("template") template: String,
        @Name("level") @MinMax(min = "1", max = "100") level: Int,
        @Name("type") type: String?
    ): Int {
        if (context.source.player != null) {
            val gymDto = GymManager.GYM_TEMPLATES[template]

            if (gymDto == null) {
                context.source.sendError(
                    translatable(
                        modId("message.error.command.debug_reward.no_template").toTranslationKey(),
                        template
                    )
                )
                return -1
            }

            level.apply {
                this.coerceIn(1..100)
            }

            val gymType = when (type) {
                null -> ElementalTypes.all().random().name
                else -> type
            }

            GymManager.handleLootDistribution(
                context.source.playerOrThrow,
                GymTemplate.fromGymDto(gymDto, level, type),
                level,
                gymType
            )

            context.source.sendMessage(
                translatable(
                    modId("message.info.command.debug_reward").toTranslationKey(),
                    context.source.playerOrThrow.name
                )
            )
        } else {
            context.source.sendError(
                translatable(modId("message.error.command.debug_reward.no_player").toTranslationKey())
            )
            return -1
        }
        return 1
    }

    @Command("debug_cache")
    @RequiresPermissionLevel(4)
    fun debugCache(
        context: CommandContext<ServerCommandSource>,
        @Name("type") type: String,
        @Name("rarity") rarity: String
    ): Int {
        if (context.source.player != null) {
            try {
                val rarityEnum = Rarity.valueOf(rarity.uppercase())
                val typeEnum = ElementalTypes.get(type) ?: throw RuntimeException("cannot get elemental type: $type")
                runOnServer {
                    val poke: Pokemon = CacheHandler.getPoke(
                        typeEnum,
                        rarityEnum,
                        context.source.player!!,
                        addToParty = true
                    )
                    context.source.player!!.sendMessage(
                        literal("Rolled $rarity $type ${poke.species.name} shiny: ${poke.shiny}")
                    )
                }
            } catch (e: Exception) {
                context.source.player!!.sendMessage(
                    literal("Cannot generate $rarity $type poke, caught error: ${e.message}")
                )
                return -1
            }
        } else {
            context.source.sendError(
                literal("Cannot generate reward for ${context.source.displayName}")
            )
            return -1
        }
        return 1
    }

    fun register() {
        debug("Registering chat commands")
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, registryAccess, _ ->
                MinecraftAdmiral
                    .builder(dispatcher, registryAccess)
                    .addCommandClasses(this::class.java)
                    .build()
            }
        )
    }
}
