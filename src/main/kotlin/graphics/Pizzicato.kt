package graphics


external interface SoundSpecifier {
    var source: String
    var options: SoundOptions
}

fun SoundSpecifier(): SoundSpecifier = js("{}")

external interface SoundOptions {
    var type: String
    var frequency: Double
}

fun SoundOptions(): SoundOptions = js("{}")

external class Pizzicato {
    companion object {
        class Sound(sound: SoundSpecifier) {
            fun play(time: Double = definedExternally, offset: Double = definedExternally)
            fun stop()
        }
    }
}