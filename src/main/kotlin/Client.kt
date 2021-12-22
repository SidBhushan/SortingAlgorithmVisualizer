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
import kotlin.random.Random

const val MAX_ITEM: Int = 50

fun main() {
    val array = DoubleArray(50)
    for (index in array.indices) {
        array[index] = Random.nextDouble(1.0, MAX_ITEM.toDouble())
    }
    val sortingArray = SortingArray(array)

    window.onload = {
        document.getElementById("controls")?.append {
            select {
                id = "sorting-select"
                sortingOption("Bubble Sort", "bubbleSort")
                sortingOption("Cocktail Shaker Sort", "cocktailShakerSort")
                sortingOption("Insertion Sort", "insertionSort")
                sortingOption("Selection Sort", "selectionSort")
                sortingOption("Shell Sort", "shellSort")
                sortingOption("Quicksort", "quickSort")
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
                    when ((document.getElementById("sorting-select") as? HTMLSelectElement)?.value) {
                        "bubbleSort" -> bubbleSort(sortingArray)
                        "cocktailShakerSort" -> cocktailShakerSort(sortingArray)
                        "insertionSort" -> insertionSort(sortingArray)
                        "selectionSort" -> selectionSort(sortingArray)
                        "shellSort" -> shellSort(sortingArray)
                        "quickSort" -> quickSort(sortingArray)
                        else -> return@onclick
                    }
                    startButton?.classList?.add("disabled")
                    reverseButton?.classList?.remove("disabled")
                }
            }
            button (classes = "disabled") {
                id = "reverse-button"
                +"\u21ba"
                onClickFunction = onclick@ {
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
            val renderer = Renderer(sortingArray, root)
            renderer.start()
        }
    }
}