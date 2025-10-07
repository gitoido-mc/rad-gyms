/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui.screen

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.client.gui.CobblemonRenderable
import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.api.context.LevelBoundsContext
import lol.gito.radgyms.api.event.LevelBoundsCallback
import lol.gito.radgyms.client.gui.widget.LevelSliderWidget
import lol.gito.radgyms.client.radGymsResource
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.network.payload.GymEnterC2S
import lol.gito.radgyms.common.util.TranslationUtil.buildTypeText
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Colors
import net.minecraft.util.math.BlockPos


@Environment(EnvType.CLIENT)
class GymEnterScreen(val key: Boolean, val type: String? = null, val pos: BlockPos? = null) : CobblemonRenderable,
    Screen(
        when {
            (type == null || ElementalTypes.get(type) != null) -> translatable(
                modId("gui.common.set-gym-level").toTranslationKey(),
                buildTypeText(type)
            )

            else -> translatable(
                modId("gui.common.set-custom-gym-level").toTranslationKey(),
                buildTypeText(type)
            )
        }
    ) {
    companion object {
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        private val panelResource = radGymsResource("textures/gui/gym_enter.png")
    }

    val middleX: Int
        get() = this.client!!.window.scaledWidth / 2
    val middleY: Int
        get() = this.client!!.window.scaledHeight / 2

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    var level: Int = 10

    val usesLeft: Int? = when (pos) {
        null -> null
        else -> {
            val player = MinecraftClient.getInstance().player
            val gymEntrance: GymEntranceEntity = player!!.world.getBlockEntity(pos) as GymEntranceEntity
            gymEntrance.usesLeftForPlayer(player)
        }
    }

    override fun applyBlur(delta: Float) {}

    override fun renderDarkening(context: DrawContext) {}

    override fun close() = this.client!!.setScreen(null)

    override fun init() {
        val defaultMin = 10
        val defaultMax = Cobblemon.config.maxPokemonLevel

        val res = LevelBoundsCallback.EVENT.invoker().onDecideBounds(LevelBoundsContext(defaultMin, defaultMax))

        val minLevel = res.minOverride() ?: defaultMin;
        val maxLevel = res.maxOverride() ?: defaultMax;

        this.level = this.level.coerceIn(minLevel, maxLevel)

        val levelSelectSlider = LevelSliderWidget(
            x = leftX + 55,
            y = topY + 25,
            minLevel,
            maxLevel
        ) { level ->
            this.level = level
            this.tick()
        }

        val decButton = ButtonWidget
            .builder(Text.of("-1")) {
                level = level.dec().coerceIn(minLevel, maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(20, 20)
            .position(leftX + 35, topY + 25)
            .build()

        val dec10Button = ButtonWidget
            .builder(Text.of("-10")) {
                level = level.minus(10).coerceIn(minLevel, maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(25, 20)
            .position(leftX + 10, topY + 25)
            .build()

        val incButton = ButtonWidget
            .builder(Text.of("+1")) {
                level = level.inc().coerceIn(minLevel, maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(20, 20)
            .position(leftX + 245, topY + 25)
            .build()

        val inc10Button = ButtonWidget
            .builder(Text.of("+10")) {
                level = level.plus(10).coerceIn(minLevel, maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(25, 20)
            .position(leftX + 265, topY + 25)
            .build()

        val proceedButton = ButtonWidget
            .builder(ScreenTexts.PROCEED) {
                debug(level.toString())
                ClientPlayNetworking.send(GymEnterC2S(key, level, type, pos))
                close()
            }
            .size(50, 20)
            .position(leftX + 10, topY + (BASE_HEIGHT - 30))
            .build()

        val cancelButton = ButtonWidget
            .builder(ScreenTexts.CANCEL) { close() }
            .size(50, 20)
            .position(leftX + (BASE_WIDTH - 60), topY + (BASE_HEIGHT - 30))
            .build()

        this.addDrawableChild(levelSelectSlider)
        this.addDrawableChild(dec10Button)
        this.addDrawableChild(decButton)
        this.addDrawableChild(incButton)
        this.addDrawableChild(inc10Button)
        this.addDrawableChild(proceedButton)
        this.addDrawableChild(cancelButton)

        super.init()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val matrices = context.matrices
        super.renderInGameBackground(context)

        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        blitk(
            matrixStack = matrices,
            texture = panelResource,
            x = x, y = y,
            width = BASE_WIDTH,
            height = BASE_HEIGHT
        )

        val message = when (pos) {
            null -> translatable("rad-gyms.gui.common.set-gym-level", buildTypeText(type))
            else -> {
                translatable(
                    "rad-gyms.gui.common.set-gym-level-entry",
                    buildTypeText(type),
                    usesLeft
                )
            }
        }

        // Box Label
        drawScaledText(
            context = context,
            text = message,
            x = x + (BASE_WIDTH / 2),
            y = y + 10,
            centered = true,
            colour = Colors.BLACK
        )

        super.render(context, mouseX, mouseY, delta)
    }
}