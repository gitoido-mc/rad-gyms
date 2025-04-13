package lol.gito.radgyms.cache

import kotlin.random.Random

class WeightedRandomSelector(private val items: Map<String, Int>) {
    private val size: Int
    private val probabilities: DoubleArray
    private val aliases: IntArray

    init {
        require(items.all { it.value >= 0 }) { "Weights cannot be negative" }
        require(items.filter { it.value > 0 }.isNotEmpty()) { "At least one weight must be positive" }

        size = items.size
        probabilities = DoubleArray(size)
        aliases = IntArray(size)
        val sum = items.map { it.value }.sum()

        val scaledWeights = items.map { it.value * size / sum }.toMutableList()
        val small = mutableListOf<Int>()
        val large = mutableListOf<Int>()

        for (i in 0 until  size) {
            if (scaledWeights[i] < 1.0) small.add(i) else large.add(i)
        }

        while (small.isNotEmpty() && large.isNotEmpty()) {
            val l = small.removeAt(small.size - 1)
            val g = large.removeAt(large.size - 1)

            probabilities[l] = scaledWeights[l].toDouble()
            aliases[l] = g

            scaledWeights[g] = ((scaledWeights[g] + scaledWeights[l]) - 1.0).toInt()

            if (scaledWeights[g] < 1.0) small.add(g) else large.add(g)
        }

        while (large.isNotEmpty()) {
            probabilities[large.removeAt(large.size - 1)] = 1.0
        }

        while (small.isNotEmpty()) {
            probabilities[small.removeAt(small.size - 1)] = 1.0
        }
    }

    fun next(): String {
        val column = Random.nextInt(size)
        val coinToss = Random.nextDouble()
        val pos = if (coinToss < probabilities[column]) column else aliases[column]
        return items.toList()[pos].first
    }
}
