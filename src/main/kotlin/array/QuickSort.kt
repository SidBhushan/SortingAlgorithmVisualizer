package array

fun quicksort(array: SortingArray) {
    fun partition(array: SortingArray, low: Int, high: Int): Int {
        val pivot = array[high]
        var i = low - 1
        for (j in low until high) {
            if (array[j] <= pivot) {
                i++
                array.switch(i, j)
            }
        }
        i++
        array.switch(i, high)
        return i
    }

    fun quicksort(array: SortingArray, low: Int, high: Int) {
        if (low >= high || low < 0 || high >= array.size) {
            return
        }

        val p = partition(array, low, high)

        quicksort(array, low, p - 1)
        quicksort(array, p, high)
    }

    quicksort(array, 0, array.size - 1)
}