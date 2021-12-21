package graphics

import MAX_ITEM
import array.SortingArray
import graphics.Pizzicato.Companion.Sound as Sound
import graphics.svg.rect
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.html.svg
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import kotlin.coroutines.CoroutineContext

const val RECT_MOVE_SPEED: Double = 1.0
const val HEIGHT: Double = 1000.0
const val WIDTH: Double = 1000.0
const val LOW_PITCH: Double = 130.81
const val HIGH_PITCH: Double = 523.25
const val SOUND_DURATION: Long = 250

class Renderer(val array: SortingArray, val root: Node) : CoroutineScope {
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
                for ((index, value) in array.vals.withIndex()) {
                    val nextRect = rect("0", yScale(index), rectScale.width(value), rectScale.height) {
                        attributes["stroke"] = colorScale(value)
                        attributes["fill"] = colorScale(value)
                    }
                    rects.add(nextRect)
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
//                delay(SOUND_DURATION)
//                sound.stop()
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

    private fun animate(rect: HTMLElement, block: suspend CoroutineScope.(HTMLElement) -> Unit): Animation =
        Animation(rect, block)

    private fun moveRect(rect: HTMLElement, currentHeight: Double, targetHeight: Double): Animation =
        animate(rect) {
            val timer = AnimationTimer()
            var y = currentHeight
            val speed = if (targetHeight - y > 0) RECT_MOVE_SPEED else -RECT_MOVE_SPEED
            while (true) {
                val dt = timer.await() * speed
                y += dt
                rect.setAttribute("y", y.toString())
                if ((speed > 0 && y >= targetHeight) || (speed < 0 && y <= targetHeight)) {
                    rect.setAttribute("y", targetHeight.toString())
                    break
                }
            }
        }

    fun switchRects(index1: Int, index2: Int, soundValue: Double) {
        val rect1 = rects[index1]
        val rect2 = rects[index2]
        rects[index1] = rect2
        rects[index2] = rect1
        val rect1Height = yScale(index1).toDouble()
        val rect2Height = yScale(index2).toDouble()
        val animation1 = moveRect(rect1, rect1Height, rect2Height)
        val animation2 = moveRect(rect2, rect2Height, rect1Height)
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