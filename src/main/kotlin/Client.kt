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
    val array = DoubleArray(30)
    for (index in array.indices) {
        array[index] = (1..MAX_ITEM).random().toDouble()
//        array[index] = Random.nextDouble(1.0, MAX_ITEM.toDouble())
    }
    val sortingArray = SortingArray(array)

    window.onload = {
        val controls = document.getElementById("controls")
        if (controls != null) {
            controls.append {
                select {
                    id = "sorting-select"
                    sortingOption("Bubble Sort", "bubbleSort")
                    sortingOption("Cocktail Shaker Sort", "cocktailShakerSort")
                    sortingOption("Insertion Sort", "insertionSort")
                    sortingOption("Selection Sort", "selectionSort")
                    sortingOption("Quicksort", "quickSort")
                }
            }
            controls.append {
                button {
                    id = "start-button"
                    +"Start"
                    onClickFunction = onclick@{
                        val button = document.getElementById("start-button")
                        if (button?.classList?.contains("disabled") == true) {
                            return@onclick
                        }
                        when ((document.getElementById("sorting-select") as? HTMLSelectElement)?.value) {
                            "bubbleSort" -> bubbleSort(sortingArray)
                            "cocktailShakerSort" -> cocktailShakerSort(sortingArray)
                            "insertionSort" -> insertionSort(sortingArray)
                            "selectionSort" -> selectionSort(sortingArray)
                            "quickSort" -> quicksort(sortingArray)
                            else -> return@onclick
                        }
                        println(sortingArray.vals)
                        button?.classList?.add("disabled")
                    }
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