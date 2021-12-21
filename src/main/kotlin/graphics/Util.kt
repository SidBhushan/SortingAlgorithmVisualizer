package graphics

import kotlinx.html.TagConsumer
import kotlinx.html.option
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.sortingOption(name: String, value: String) = option {
    +name
    this.value = value
}