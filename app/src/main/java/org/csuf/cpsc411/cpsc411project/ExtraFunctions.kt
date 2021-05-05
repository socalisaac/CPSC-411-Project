package org.csuf.cpsc411.cpsc411project

import android.graphics.Color
import kotlin.math.roundToInt

fun darkenColor(color: Int, undoDarken: Boolean): Int {
    val factor = 0.8
    val a = Color.alpha(color)
    val r: Int
    val g: Int
    val b: Int

    if(undoDarken){
        r = (Color.red(color) / factor).roundToInt()
        g = (Color.green(color) / factor).roundToInt()
        b = (Color.blue(color) / factor).roundToInt()
    }
    else {
        r = (Color.red(color) * factor).roundToInt()
        g = (Color.green(color) * factor).roundToInt()
        b = (Color.green(color) * factor).roundToInt()
    }
        return Color.argb(a,
            r.coerceAtMost(255),
            g.coerceAtMost(255),
            b.coerceAtMost(255))
}