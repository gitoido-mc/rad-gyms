package lol.gito.radgyms.common.util.math

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec2
import kotlin.math.roundToInt

class Vec2i {
    val x: Int
    val y: Int

    @JvmField
    val ZERO: Vec2i = Vec2i(0, 0)

    @JvmField
    val ONE: Vec2i = Vec2i(1, 1)

    @JvmField
    val UNIT_X: Vec2i = Vec2i(1, 0)

    @JvmField
    val NEG_UNIT_X: Vec2i = Vec2i(-1, 0)

    @JvmField
    val UNIT_Y: Vec2i = Vec2i(0, 1)

    @JvmField
    val NEG_UNIT_Y: Vec2i = Vec2i(0, -1)

    @JvmField
    val MAX: Vec2i = Vec2i(Int.MAX_VALUE, Int.MAX_VALUE)

    @JvmField
    val MIN: Vec2i = Vec2i(Int.MIN_VALUE, Int.MIN_VALUE)

    constructor(x: Number, y: Number) {
        this.x = when (x) {
            is Double -> x.roundToInt()
            is Float -> x.roundToInt()
            is Long -> x.toInt()
            is Short -> x.toInt()
            is Byte -> x.toInt()
            is Int -> x
            else -> throw TypeCastException("Cannot convert $x to Int")
        }
        this.y = when (y) {
            is Double -> y.roundToInt()
            is Float -> y.roundToInt()
            is Long -> y.toInt()
            is Short -> y.toInt()
            is Byte -> y.toInt()
            is Int -> y
            else -> throw TypeCastException("Cannot convert $y to Int")
        }
    }

    fun scale(f: Float): Vec2i {
        return Vec2i(x * f, y * f)
    }

    fun dot(vec2: Vec2i): Int {
        return x * vec2.x + y * vec2.y
    }

    fun add(vec2: Vec2): Vec2i {
        return Vec2i(x + vec2.x, y + vec2.y)
    }

    fun add(f: Float): Vec2i {
        return Vec2i(x + f, y + f)
    }

    fun equals(vec2: Vec2i): Boolean {
        return x == vec2.x && y == vec2.y
    }

    fun normalized(): Vec2i? {
        val f = Mth.sqrt((x * x + y * y).toFloat())
        return if (f < 1.0E-4f) ZERO else Vec2i(x / f, y / f)
    }

    fun length(): Float {
        return Mth.sqrt((x * x + y * y).toFloat())
    }

    fun lengthSquared(): Int {
        return this.x * this.x + this.y * this.y
    }

    fun distanceToSqr(vec2: Vec2): Float {
        val f: Float = vec2.x - this.x
        val g: Float = vec2.y - this.y
        return f * f + g * g
    }

    fun negated(): Vec2i {
        return Vec2i(-this.x, -this.y)
    }
}