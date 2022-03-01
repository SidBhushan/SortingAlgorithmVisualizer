package array.algorithms

import array.SortingArray

fun insertionSort(array: SortingArray) {
    for (i in 0 until array.size - 1) {
        for (j in i + 1 downTo 1) {
            if (array[j] < array[j - 1]) {
                array.switch(j, j - 1)
            }
        }
    }
}