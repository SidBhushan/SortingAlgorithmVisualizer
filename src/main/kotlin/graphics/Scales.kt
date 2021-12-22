package graphics

import kotlin.math.exp
import kotlin.math.ln

const val saturation = "100%"
const val lightness = "50%"

class ColorScale(private val min: Double, private val max: Double) {
    operator fun invoke(num: Double): String {
        val hue = (num - min) / (max - min) * 360
        return "hsl($hue, $saturation, $lightness)"
    }
}

class YScale(private val max: Double, private val items: Int) {
    operator fun invoke(index: Int): String = ((index.toDouble() / items) * max).toString()
}

class RectScale(
    private val areaWidth: Double,
    private val areaHeight: Double,
    private val items: Int,
    private val max: Double
) {
    val height: String
        get() = (areaHeight / (2 * items)).toString()

    fun width(num: Double): String = ((num / max) * areaWidth).toString()
}

class FreqScale(private val low: Double, private val high: Double, private val max: Double) {
    private val scaleFactor = (ln(high) - ln(low)) / max
    operator fun invoke(value: Double): Double = low * exp(scaleFactor * value)
}