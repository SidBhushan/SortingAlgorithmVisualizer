import array.SortingArray
import array.bubbleSort
import array.quicksort
import graphics.Renderer
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.classes
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.button
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.option
import kotlinx.html.js.select
import kotlinx.html.onClick
import org.w3c.dom.HTMLSelectElement
import kotlin.random.Random

const val MAX_ITEM: Int = 50

fun main() {
    val array = DoubleArray(30)
    for (index in array.indices) {
        array[index] = Random.nextDouble(1.0, MAX_ITEM.toDouble())
    }
    val sortingArray = SortingArray(array)

    window.onload = {
        val controls = document.getElementById("controls")
        if (controls != null) {
            controls.append {
                select {
                    id = "sorting-select"
                    option {
                        +"Bubble Sort"
                        value = "bubbleSort"
                    }
                    option {
                        +"Quicksort"
                        value = "quickSort"
                    }
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
                            "quickSort" -> quicksort(sortingArray)
                            else -> return@onclick
                        }
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