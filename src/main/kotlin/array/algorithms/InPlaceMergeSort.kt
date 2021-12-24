package array.algorithms

import array.SortingArray
import kotlin.math.ceil

fun inPlaceMergeSort(array: SortingArray) {
    fun merge(array: SortingArray, start: Int, end: Int) {
        for (gap in InPlaceMergeSortGap(end - start + 1)) {
            println(gap)
            for (i in start..end - gap) {
                if (array[i] > array[i + gap]) {
                    array.switch(i, i + gap)
                }
            }
        }
    }

    fun mergeSort(array: SortingArray, start: Int, end: Int) {
        if (start >= end) {
            return
        }

        val mid =(start + end) / 2
        mergeSort(array, start, mid)
        mergeSort(array, mid + 1, end)
        merge(array, start, end)
    }

    mergeSort(array, 0, array.size - 1)
}

class InPlaceMergeSortGap(private val gap: Int) {
    class GapIterator(var gap: Int) : Iterator<Int> {
        override fun hasNext(): Boolean = gap > 1
        override fun next(): Int {
            if (gap == 1) return 0
            gap = ceil(gap / 2.0).toInt()
            return gap
        }
    }

    operator fun iterator() = GapIterator(gap)
}