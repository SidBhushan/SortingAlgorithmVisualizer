package array.algorithms

import array.SortingArray

fun heapSort(array: SortingArray) {
    fun heapify(array: SortingArray, rootIndex: Int, arraySize: Int) {
        val leftChildIndex = 2 * rootIndex + 1
        val rightChildIndex = 2 * rootIndex + 2
        val largestIndex = listOf(rootIndex, leftChildIndex, rightChildIndex).maxByOrNull {
            if (it < arraySize) array[it] else Double.MIN_VALUE
        }

        if (largestIndex != null && largestIndex != rootIndex) {
            array.switch(rootIndex, largestIndex)
            heapify(array, largestIndex, arraySize)
        }
    }

    for (i in array.size / 2 - 1 downTo 0) {
        heapify(array, i, array.size)
    }

    for (i in array.size - 1 downTo 1) {
        array.switch(0, i)
        heapify(array, 0, i)
    }
}