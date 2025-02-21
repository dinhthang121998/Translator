package com.example.translator.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.translator.domain.model.TextDrawing

class OverlayView(context: Context, attributes: AttributeSet) : View(context, attributes) {

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
    }
    private val paintBoundBox = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10F
        color = Color.GRAY
    }

    private var listTextDrawing: List<TextDrawing>? = null

    fun updateTextDrawing(listTextDrawing: List<TextDrawing>) {
        this.listTextDrawing = listTextDrawing
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val listRect = listTextDrawing?.map { it.rect }
        drawBoundingBox(canvas, paintBoundBox, listRect)
        drawTranslatedText(canvas, paintText, listTextDrawing)
    }

    private fun drawTranslatedText(canvas: Canvas, paint: Paint, listText: List<TextDrawing>?) {
        listText?.forEach { textDrawingItem ->
            textDrawingItem.rect?.let { rect ->
                paint.textSize = rect.height().toFloat()
                canvas.drawText(
                    textDrawingItem.textLine,
                    rect.left.toFloat(),
                    rect.top.toFloat() + rect.height(),
                    paint
                )
            }
        }
    }

    private fun drawBoundingBox(canvas: Canvas, paint: Paint, listRect: List<Rect?>?) {
        listRect?.forEach { rect ->
            rect?.let {
                canvas.drawRect(it, paint)
            }
        }
    }

}