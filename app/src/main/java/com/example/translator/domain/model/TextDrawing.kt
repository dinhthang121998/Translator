package com.example.translator.domain.model

import android.graphics.Rect

data class TextDrawing(
    val textLine: String = "",
    val rect: Rect? = Rect(),
)
