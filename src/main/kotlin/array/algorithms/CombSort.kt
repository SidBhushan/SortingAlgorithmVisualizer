package array.algorithms

import array.SortingArray
import kotlin.math.floor

fun combSort(array: SortingArray) {
    for (gap in CombSortGap(array.size)) {
        for (i in 0 until array.size - gap) {
            if (array[i] > array[i + gap]) {
                array.switch(i, i + gap)
            }
        }
    }
}

class CombSortGap(private val gap: Int) {
    class GapIterator(var gap: Int) : Iterator<Int> {
        override fun hasNext(): Boolean = gap > 1
        override fun next(): Int {
            gap = floor(gap / 1.3).toInt()
            return gap
        }
    }

    operator fun iterator() = GapIterator(gap)
}