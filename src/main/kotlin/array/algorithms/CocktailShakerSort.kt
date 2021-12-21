package array.algorithms

import array.SortingArray

fun cocktailShakerSort(array: SortingArray) {
    var end = array.size
    for (i in 0 until array.size / 2) {
        for (j in i until end) {
            if (array[j] > array[j + 1]) {
                array.switch(j, j + 1)
            }
        }
        for (j in end - 1 downTo i + 1) {
            if (array[j] < array[j - 1]) {
                array.switch(j, j - 1)
            }
        }
        end--
    }
}