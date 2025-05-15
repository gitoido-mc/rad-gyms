package lol.gito.radgyms.item

import com.cobblemon.mod.common.Cobblemon
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gui.GuiHandler
import lol.gito.radgyms.item.dataComponent.DataComponentManager.CACHE_SHINY_BOOST_COMPONENT
import lol.gito.radgyms.item.dataComponent.DataComponentManager.GYM_TYPE_COMPONENT
import lol.gito.radgyms.item.group.ItemGroupManager
import lol.gito.radgyms.network.NetworkStackHandler
import lol.gito.radgyms.util.TranslationUtil.attuneType
import net.minecraft.component.DataComponentTypes.RARITY
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

const val RG_CACHE_BLOCK_BOOST = 9

class CommonPokeCache : PokeCache(Rarity.COMMON)
class UncommonPokeCache : PokeCache(Rarity.UNCOMMON)
class RarePokeCache : PokeCache(Rarity.RARE)
class EpicPokeCache : PokeCache(Rarity.EPIC)

open class PokeCache(private val rarity: Rarity) : Item(
    Settings()
        .group(ItemGroupManager.GYMS_GROUP)
        .rarity(rarity)
        .stackGenerator(ItemGroupManager::cacheItemStacks)
) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val offhand = user.offHandStack
        val boost = stack.getOrDefault(CACHE_SHINY_BOOST_COMPONENT, 0)
        val type = stack.getOrDefault(GYM_TYPE_COMPONENT, null)
        if (offhand.isOf(Items.LAPIS_LAZULI) && !world.isClient && boost < Cobblemon.config.shinyRate) {
            stack.set(CACHE_SHINY_BOOST_COMPONENT, boost.plus(CONFIG.lapisBoostAmount))
        }
        if (offhand.isOf(Items.LAPIS_BLOCK) && !world.isClient && boost < Cobblemon.config.shinyRate) {
            val newBoost = when (boost.plus(RG_CACHE_BLOCK_BOOST) > Cobblemon.config.shinyRate) {
                true -> Cobblemon.config.shinyRate.toInt()
                false -> boost.plus(RG_CACHE_BLOCK_BOOST * CONFIG.lapisBoostAmount)
                    .coerceAtMost(Cobblemon.config.shinyRate.toInt() - 1)
            }
            stack.set(CACHE_SHINY_BOOST_COMPONENT, newBoost)
        }

        if (offhand.isOf(Items.LAPIS_LAZULI) || offhand.isOf(Items.LAPIS_BLOCK)) {
            offhand.decrementUnlessCreative(1, user)
            return TypedActionResult.success(user.getStackInHand(hand), true)
        }

        if (world.isClient) {
            if (type == null) {
                GuiHandler.openCacheAttuneScreen(
                    user,
                    this.rarity,
                    user.getStackInHand(hand).getOrDefault(CACHE_SHINY_BOOST_COMPONENT, 0)
                )
            } else {
                CHANNEL.clientHandle().send(
                    NetworkStackHandler.CacheOpen(
                        type = type,
                        rarity = rarity,
                        shinyBoost = boost,
                    )
                )
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand), true)
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType
    ) {
        val shinyBoost = stack.get(CACHE_SHINY_BOOST_COMPONENT)
        val cacheType = stack.get(GYM_TYPE_COMPONENT)
        if (shinyBoost != null && shinyBoost > 0) {
            val tooltipText = translatable(
                modId("item.component.shiny_boost").toTranslationKey(),
                shinyBoost.toString()
            )

            tooltip.addLast(tooltipText.formatted(Formatting.GOLD).formatted(Formatting.BOLD))
        }

        if (cacheType != null) {
            val tooltipText: MutableText = attuneType(cacheType)
            tooltip.addLast(tooltipText.styled { it.withColor(Formatting.GOLD) })
        } else {
            val tooltipText: MutableText = translatable(
                modId("item.component.gym_type").toTranslationKey(),
                translatable(modId("item.component.type.chaos").toTranslationKey()).styled {
                    it.withFormatting(Formatting.OBFUSCATED)
                }
            )
            tooltip.addLast(
                tooltipText.styled {
                    it.withColor(Formatting.DARK_PURPLE)
                }
            )
        }
    }

    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.set(RARITY, this.rarity)
        stack.set(CACHE_SHINY_BOOST_COMPONENT, 0)

        return stack
    }

    override fun hasGlint(stack: ItemStack): Boolean {
        return stack.getOrDefault(CACHE_SHINY_BOOST_COMPONENT, 0) > 0
    }
}
