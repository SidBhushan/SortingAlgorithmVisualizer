package array.algorithms

import array.SortingArray

fun oddEvenSort(array: SortingArray) {
    for (n in 0 until array.size - 1) {
        for (i in 1 until array.size - 1 step 2) {
            if (array[i] > array[i + 1]) {
                array.switch(i, i + 1)
            }
        }
        for (i in 0 until array.size - 1 step 2) {
            if (array[i] > array[i + 1]) {
                array.switch(i, i + 1)
            }
        }
    }
}