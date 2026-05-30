package com.bzapata.triangle.deltaCoreKt.deltaCore.emulatorCore.video

import android.graphics.PixelFormat
import android.util.Size
import java.text.Format

data class VideoFormat(
        var format : Format,
        var dimensions : Size
){
    sealed class Format {
        data class Bitmap(val pixelFormat : PixelFormat) : Format()
        data object OpenGLES2 : Format()
        data object OpenGLES3 : Format()
    }

    val pixelFormat : PixelFormat
        get() = (format as Format.Bitmap).pixelFormat

    val bufferSize : Int
        get() = dimensions.height * dimensions.width * pixelFormat.bytesPerPixel

}
enum class PixelFormat(
    val bytesPerPixel : Int
) {
    RGB585(2),
    BGRA8(4),
    RGBA8(4)
}