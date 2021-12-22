package array

import graphics.Renderer

class SortingArray(val vals: DoubleArray) {
    val max: Double
        get() = vals.maxOrNull() ?: 0.0

    val min: Double
        get() = vals.minOrNull() ?: 0.0

    val size: Int
        get() = vals.size

    lateinit var renderer: Renderer
    private val swaps: ArrayDeque<Pair<Int, Int>> = ArrayDeque()

    operator fun get(index: Int) = vals[index]

    fun switch(index1: Int, index2: Int) {
        val temp = vals[index1]
        vals[index1] = vals[index2]
        vals[index2] = temp
        renderer.switchRects(index1, index2, 2 / (1 / vals[index1] + 1 / vals[index2]))
        swaps.addFirst(Pair(index1, index2))
    }

    fun reverse() {
        while (swaps.isNotEmpty()) {
            val (index1, index2) = swaps.removeFirst()
            val temp = vals[index1]
            vals[index1] = vals[index2]
            vals[index2] = temp
            renderer.switchRects(index1, index2, 2 / (1 / vals[index1] + 1 / vals[index2]), true)
        }
    }
}