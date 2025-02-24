package com.example.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.common.R
import com.example.model.TextDrawing
import kotlin.math.min

class OverlayView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private val paintText =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.black)
        }
    private val paintBoundBox =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 10F
            color = context.getColor(R.color.white)
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

    private fun drawTranslatedText(
        canvas: Canvas,
        paint: Paint,
        listText: List<TextDrawing>?,
    ) {
        listText?.forEach { textDrawingItem ->
            textDrawingItem.rect?.let { rect ->
                paint.textSize = min(rect.height().toFloat(), rect.width().toFloat())
                if (isHorizontal(rect)) {
                    drawTextHorizontal(canvas, paintText, rect, textDrawingItem.textLine)
                } else {
                    // drawing text in vertical, for example: Japanese
                    drawingTextVertical(canvas, paintText, rect, textDrawingItem.textLine, ROTATION_VERTICAL)
                }
            }
        }
    }

    private fun drawingTextVertical(
        canvas: Canvas,
        paint: Paint,
        rect: Rect,
        text: String,
        rotation: Float,
    ) {
        canvas.save()
        canvas.rotate(rotation, rect.left.toFloat(), rect.top.toFloat())
        canvas.drawText(
            text,
            rect.left.toFloat(),
            rect.top.toFloat(),
            paint,
        )
        canvas.restore()
    }

    private fun drawTextHorizontal(
        canvas: Canvas,
        paint: Paint,
        rect: Rect,
        text: String,
    ) {
        canvas.drawText(
            text,
            rect.left.toFloat(),
            rect.top.toFloat() + rect.height(),
            paint,
        )
    }

    private fun isHorizontal(rect: Rect): Boolean {
        return rect.height() < rect.width()
    }

    private fun drawBoundingBox(
        canvas: Canvas,
        paint: Paint,
        listRect: List<Rect?>?,
    ) {
        listRect?.forEach { rect ->
            rect?.let {
                canvas.drawRect(it, paint)
            }
        }
    }

    companion object {
        private const val ROTATION_VERTICAL = 90F
    }
}
