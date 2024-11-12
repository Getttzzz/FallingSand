package com.yuriihetsko.fallingsand

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

class HSBColor(val hue: Float, val saturation: Float, val brightness: Float) {

    // Convert HSB to ARGB and return as a Compose Color
    fun toColor(): Color {
        val rgb = hsbToRgb(hue, saturation, brightness)
        return Color(rgb[0], rgb[1], rgb[2], 1f) // RGB values in Color() must be normalized from 0f to 1f
    }

    private fun hsbToRgb(hue: Float, saturation: Float, brightness: Float): FloatArray {
        val h = (hue % 360) / 60f
        val s = saturation.coerceIn(0f, 1f)
        val b = brightness.coerceIn(0f, 1f)

        val c = b * s
        val x = c * (1 - kotlin.math.abs(h % 2 - 1))
        val m = b - c

        val (r, g, bl) = when (h.toInt()) {
            0 -> listOf(c, x, 0f)
            1 -> listOf(x, c, 0f)
            2 -> listOf(0f, c, x)
            3 -> listOf(0f, x, c)
            4 -> listOf(x, 0f, c)
            5 -> listOf(c, 0f, x)
            else -> listOf(0f, 0f, 0f) // default case, shouldn't reach here
        }

        return floatArrayOf(r + m, g + m, bl + m) // Convert to [0, 1] range for Color()
    }
}
