package array

import graphics.Renderer

class SortingArray(val vals: Array<Pair<Double, Char>>) {
    val max: Double
        get() = (vals.maxByOrNull { it.first })?.first ?: 0.0

    val min: Double
        get() = (vals.minByOrNull { it.first })?.first ?: 0.0

    val size: Int
        get() = vals.size

    lateinit var renderer: Renderer
    private val swaps: ArrayDeque<Pair<Int, Int>> = ArrayDeque()

    operator fun get(index: Int) = vals[index].first

    fun switch(index1: Int, index2: Int) {
        val temp = vals[index1]
        vals[index1] = vals[index2]
        vals[index2] = temp
        renderer.switchRects(index1, index2, 2 / (1 / vals[index1].first + 1 / vals[index2].first))
        swaps.addFirst(Pair(index1, index2))
    }

    fun reverse() {
        while (swaps.isNotEmpty()) {
            val (index1, index2) = swaps.removeFirst()
            val temp = vals[index1]
            vals[index1] = vals[index2]
            vals[index2] = temp
            renderer.switchRects(index1, index2, 2 / (1 / vals[index1].first + 1 / vals[index2].first), true)
        }
    }
}