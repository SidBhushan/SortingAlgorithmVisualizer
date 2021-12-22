package array.algorithms

import array.SortingArray

fun exchangeSort(array: SortingArray) {
    for (i in 0 until array.size) {
        for (j in i + 1 until array.size) {
            if (array[j] < array[i]) {
                array.switch(i, j)
            }
        }
    }
}