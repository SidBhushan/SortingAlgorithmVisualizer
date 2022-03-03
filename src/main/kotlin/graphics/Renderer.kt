package graphics

import MAX_ITEM
import array.SortingArray
import graphics.svg.g
import graphics.Pizzicato.Companion.Sound as Sound
import graphics.svg.rect
import graphics.svg.text
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.html.svg
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.get
import kotlin.coroutines.CoroutineContext

const val RECT_MOVE_SPEED: Double = 1.0
const val FAST_SPEED: Double = 5.0
const val HEIGHT: Double = 1000.0
const val WIDTH: Double = 1000.0
const val LOW_PITCH: Double = 65.41
const val HIGH_PITCH: Double = 1046.50

class Renderer(val array: SortingArray, val root: Node, val displayText: String? = null) : CoroutineScope {
    val colorScale = ColorScale(array.min, array.max)
    val yScale = YScale(HEIGHT, array.size)
    val rectScale = RectScale(WIDTH, HEIGHT, array.size, MAX_ITEM.toDouble())
    val freqScale = FreqScale(LOW_PITCH, HIGH_PITCH, MAX_ITEM.toDouble())

    val rects: MutableList<HTMLElement> = mutableListOf()

    private var animationQueue = ArrayDeque<Triple<Animation, Animation, Sound>>()
    private var animationInProgress = false

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job

    init {
        array.renderer = this
    }

    fun start() {
        root.append {
            svg(classes = "array") {
                attributes["viewBox"] = "0 0 $WIDTH $HEIGHT"
                for ((index, pair) in array.vals.withIndex()) {
                    val value = pair.first
                    val char = pair.second.toString()
                    if (displayText == null) {
                        val nextRect = rect("0", yScale(index), rectScale.width(value), rectScale.height) {
                            attributes["stroke"] = colorScale(value)
                            attributes["fill"] = colorScale(value)
                        }
                        rects.add(nextRect)
                    } else {
                        val nextRect = g {
                            text("0", yScale(index)) {
                                attributes["font-size"] = (rectScale.height.toDouble() / 1.08216).toString()
                                attributes["font-weight"] = "800"
                                attributes["dominant-baseline"] = "hanging"
                                +char
                            }
                            rect(rectScale.height, yScale(index), rectScale.width(value), rectScale.height) {
                                attributes["stroke"] = colorScale(value)
                                attributes["fill"] = colorScale(value)
                            }
                        }
                        rects.add(nextRect)
                    }
                }
            }
        }
    }

    private fun popAnimationQueue() {
        if (animationQueue.isNotEmpty()) {
            animationInProgress = true
            val (animation1, animation2, sound) = animationQueue.removeFirst()

            val soundJob = launch {
                sound.play()
            }

            val block1 = animation1.block
            val job1 = launch {
                block1(animation1.rect)
                soundJob.join()
            }
            val block2 = animation2.block
            val job2 = launch {
                block2(animation2.rect)
                job1.join()
            }

            job2.invokeOnCompletion {
                sound.stop()
                animationInProgress = false
                popAnimationQueue()
            }
        }
    }

    private fun launchAnimationPair(animation1: Animation, animation2: Animation, sound: Sound) {
        val animations = Triple(animation1, animation2, sound)
        animationQueue.add(animations)
        if (!animationInProgress) {
            popAnimationQueue()
        }
    }

    private fun animate(elem: HTMLElement, block: suspend CoroutineScope.(HTMLElement) -> Unit): Animation =
        Animation(elem, block)

    private fun moveRect(rect: HTMLElement, currentHeight: Double, targetHeight: Double, fast: Boolean): Animation =
        animate(rect) {
            val timer = AnimationTimer()
            var y = currentHeight
            val speed =
                (if (targetHeight - y > 0) RECT_MOVE_SPEED else -RECT_MOVE_SPEED) * (if (fast) FAST_SPEED else 1.0)
            while (true) {
                val dt = timer.await() * speed
                y += dt
                adjustAttribute(rect, "y", y.toString())
                if ((speed > 0 && y >= targetHeight) || (speed < 0 && y <= targetHeight)) {
                    adjustAttribute(rect, "y", targetHeight.toString())
                    break
                }
            }
        }

    private fun adjustAttribute(elem: HTMLElement, name: String, value: String) {
        if (elem.tagName == "g") {
            for (i in 0 until elem.childElementCount) {
                elem.children[i]?.setAttribute(name, value)
            }
        } else {
            elem.setAttribute(name, value)
        }
    }

    fun switchRects(index1: Int, index2: Int, soundValue: Double, fast: Boolean = false) {
        val rect1 = rects[index1]
        val rect2 = rects[index2]
        rects[index1] = rect2
        rects[index2] = rect1
        val rect1Height = yScale(index1).toDouble()
        val rect2Height = yScale(index2).toDouble()
        val animation1 = moveRect(rect1, rect1Height, rect2Height, fast)
        val animation2 = moveRect(rect2, rect2Height, rect1Height, fast)
        val sound = createSound(soundValue)
        launchAnimationPair(animation1, animation2, sound)
    }

    private fun createSound(value: Double): Sound = Sound(SoundSpecifier().apply {
        source = "wave"
        options = SoundOptions().apply {
            type = "sine"
            frequency = freqScale(value)
        }
    })

}

class AnimationTimer {
    var time = window.performance.now()

    suspend fun await(): Double {
        val newTime = window.awaitAnimationFrame()
        val dt = newTime - time
        time = newTime
        return dt.coerceAtMost(200.0) // at most 200ms
    }

    fun reset(): Double {
        time = window.performance.now()
        return time
    }

    suspend fun delay(i: Int) {
        var dt = 0.0
        while (dt < i) {
            dt += await()
        }
    }
}

data class Animation(val rect: HTMLElement, val block: suspend CoroutineScope.(HTMLElement) -> Unit)