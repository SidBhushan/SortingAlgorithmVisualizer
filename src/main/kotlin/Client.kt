import array.SortingArray
import array.algorithms.*
import graphics.Renderer
import graphics.sortingOption
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.button
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.select
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.url.URLSearchParams
import kotlin.random.Random
import kotlin.random.nextInt

const val MAX_ITEM: Int = 50

val sorts = mapOf(
    "bubbleSort" to Sort("Bubble Sort", ::bubbleSort),
    "cocktailShakerSort" to Sort("Cocktail Shaker Sort", ::cocktailShakerSort),
    "oddEvenSort" to Sort("Odd-Even Sort", ::oddEvenSort),
    "exchangeSort" to Sort("Exchange Sort", ::exchangeSort),
    "insertionSort" to Sort("Insertion Sort", ::insertionSort),
    "selectionSort" to Sort("Selection Sort", ::selectionSort),
    "combSort" to Sort("Comb Sort", ::combSort),
    "shellSort" to Sort("Shell Sort", ::shellSort),
    "inPlaceMergeSort" to Sort("In Place Merge Sort", ::inPlaceMergeSort),
    "quickSort" to Sort("Quicksort", ::quickSort),
    "heapSort" to Sort("Heapsort", ::heapSort)
)

fun main() {
    val params = URLSearchParams(window.location.search)
    val text = params.get("text")

    val array = Array(text?.length ?: 50) { Pair(0.0, ' ') }
    val range = MAX_ITEM.toDouble() / array.size
    if (text == null) {
        for (index in array.indices) {
            array[index] = Pair(Random.nextDouble(1.0, MAX_ITEM.toDouble()), ' ')
        }
    } else {
        var lastValue = 1.0
        for (index in array.indices) {
            val nextValue = Random.nextDouble(lastValue, lastValue + range)
            array[index] = Pair(nextValue, text[index])
            lastValue = nextValue
        }
        repeat(text.length * 10) {
            val randIndex1 = Random.nextInt(array.indices)
            val randIndex2 = Random.nextInt(array.indices)
            val temp = array[randIndex1]
            array[randIndex1] = array[randIndex2]
            array[randIndex2] = temp
        }
    }
    val sortingArray = SortingArray(array)

    window.onload = {
        document.getElementById("controls")?.append {
            select {
                id = "sorting-select"
                for ((identifier, sort) in sorts) {
                    sortingOption(sort.name, identifier)
                }
            }
            button {
                id = "start-button"
                +"Start"
                onClickFunction = onclick@{
                    val startButton = document.getElementById("start-button")
                    val reverseButton = document.getElementById("reverse-button")
                    if (startButton?.classList?.contains("disabled") == true) {
                        return@onclick
                    }
                    val sort = sorts[(document.getElementById("sorting-select") as? HTMLSelectElement)?.value]?.func
                        ?: return@onclick
                    sort(sortingArray)
                    startButton?.classList?.add("disabled")
                    reverseButton?.classList?.remove("disabled")
                }
            }
            button(classes = "disabled") {
                id = "reverse-button"
                +"\u21ba"
                onClickFunction = onclick@{
                    val startButton = document.getElementById("start-button")
                    val reverseButton = document.getElementById("reverse-button")
                    if (reverseButton?.classList?.contains("disabled") == true) {
                        return@onclick
                    }
                    sortingArray.reverse()
                    startButton?.classList?.remove("disabled")
                    reverseButton?.classList?.add("disabled")
                }
            }
        }

        val root = document.getElementById("root")
        if (root != null) {
            val renderer = Renderer(sortingArray, root, text)
            renderer.start()
        }
    }
}