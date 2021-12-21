package array.algorithms

import array.SortingArray

fun bubbleSort(array: SortingArray) {
    for (i in array.size - 1 downTo 1) {
        for (j in 0 until i) {
            if (array[j] > array[j + 1]) {
                array.switch(j, j + 1)
            }
        }
    }
}