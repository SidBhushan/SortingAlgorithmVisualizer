package array.algorithms

import array.SortingArray
import kotlin.properties.Delegates.vetoable

fun selectionSort(array: SortingArray) {
    for (i in 0 until array.size) {
        var min: Pair<Double, Int> by vetoable(Pair(Double.MAX_VALUE, -1)) { _, (oldVal, _), (newVal, _) ->
            newVal < oldVal
        }
        for (j in i + 1 until array.size) {
            min = Pair(array[j], j)
        }
        array.switch(i, min.second)
    }
}