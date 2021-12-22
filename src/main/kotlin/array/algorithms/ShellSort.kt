package array.algorithms

import array.SortingArray

fun shellSort(array: SortingArray) {
    for (gap in ShellSortGap(array.size)) {
        for (offset in 0 until gap) {
            for (i in offset until array.size step gap) {
                for (j in i + 1 downTo offset step gap) {
                    if (array[j] < array[j - gap]) {
                        array.switch(j, j - gap)
                    }
                }
            }
        }
    }
}

class ShellSortGap(private val gap: Int) {
    class GapIterator(var gap: Int) : Iterator<Int> {
        override fun hasNext(): Boolean = gap > 1
        override fun next(): Int {
            gap /= 2
            return gap
        }
    }

    operator fun iterator() = GapIterator(gap)
}