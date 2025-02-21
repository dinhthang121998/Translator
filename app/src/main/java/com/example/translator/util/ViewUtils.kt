package com.example.translator.util

import android.graphics.Matrix
import android.graphics.Rect
import android.view.View

fun View.showOrGone(isShowed: Boolean) {
    this.visibility = if (isShowed) View.VISIBLE else View.GONE
}

fun Rect.mapBoundingBox(
    matrix: Matrix
): Rect {
    val values = FloatArray(9)
    matrix.getValues(values)

    // Scale factors
    val scaleX = values[Matrix.MSCALE_X]
    val scaleY = values[Matrix.MSCALE_Y]

    // Translation offsets
    val dx = values[Matrix.MTRANS_X]
    val dy = values[Matrix.MTRANS_Y]

    // Apply scale and translation
    val left = this.left * scaleX + dx
    val top = this.top * scaleY + dy
    val right = this.right * scaleX + dx
    val bottom = this.bottom * scaleY + dy

    return Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}
