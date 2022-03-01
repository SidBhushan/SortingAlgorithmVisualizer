package graphics.svg

import kotlinx.html.*

@Suppress("unused")
open class RECT(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("rect", consumer, initialAttributes, "http://www.w3.org/2000/svg", false, false),
    HtmlBlockInlineTag

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.rect(
    x: String? = null,
    y: String? = null,
    width: String? = null,
    height: String? = null,
    classes: String? = null,
    crossinline block: RECT.() -> Unit = {}
): T = RECT(
    attributesMapOf("x", x, "y", y, "width", width, "height", height, "class", classes), this
).visitAndFinalize(this, block)

@Suppress("unused")
open class G(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("g", consumer, initialAttributes, "http://www.w3.org/2000/svg", false, false),
    HtmlBlockInlineTag

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.g(
    classes: String? = null,
    crossinline block: G.() -> Unit = {}
): T = G(
    attributesMapOf("class", classes), this
).visitAndFinalize(this, block)

@Suppress("unused")
open class TEXT(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("text", consumer, initialAttributes, "http://www.w3.org/2000/svg", false, false),
    HtmlBlockInlineTag

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.text(
    x: String? = null,
    y: String? = null,
    classes: String? = null,
    crossinline block: TEXT.() -> Unit = {}
): T = TEXT(
    attributesMapOf("x", x, "y", y, "class", classes), this
).visitAndFinalize(this, block)

