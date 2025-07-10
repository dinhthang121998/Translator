package com.example.mlkit.overlay

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.example.model.TextDrawing
import kotlin.math.max
import kotlin.math.min

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic
    constructor(
        overlay: GraphicOverlay,
        private val listTextDrawing: List<TextDrawing>,
    ) : GraphicOverlay.Graphic(overlay) {
        private val rectPaint: Paint = Paint()
        private val textPaint: Paint
        private val labelPaint: Paint

        init {
            rectPaint.color = MARKER_COLOR
            rectPaint.style = Paint.Style.STROKE
            rectPaint.strokeWidth = STROKE_WIDTH
            textPaint = Paint()
            textPaint.color = TEXT_COLOR
            labelPaint = Paint()
            labelPaint.color = MARKER_COLOR
            labelPaint.style = Paint.Style.FILL
            // Redraw the overlay, as this graphic has been added.
            postInvalidate()
        }

        /** Draws the text block annotations for position, size, and raw value on the supplied canvas. */
        override fun draw(canvas: Canvas) {
            listTextDrawing.forEach { textDrawing ->
                val rect = RectF(textDrawing.rect)
                Log.d("AAAA", "text = ${textDrawing.textLine}")
                // If the image is flipped, the left will be translated to right, and the right to left.
                // translateX and translateY seem for cameraX only
                val x0 = translateX(rect.left)
                val x1 = translateX(rect.right)
                rect.left = min(x0, x1)
                rect.right = max(x0, x1)
                rect.top = translateY(rect.top)
                rect.bottom = translateY(rect.bottom)
                textPaint.textSize = Math.min(rect.height(), rect.width())

                // Just draw border
                canvas.drawRect(rect, rectPaint)
                // Draw background
                canvas.drawRect(
                    rect,
                    labelPaint,
                )

                if (isHorizontal(rect)) {
                    drawTextHorizontal(
                        textDrawing.textLine,
                        rect,
                        canvas,
                    )
                } else {
                    drawTextVertical(
                        textDrawing.textLine,
                        ROTATION,
                        rect,
                        canvas,
                    )
                }
            }
        }

        private fun drawTextHorizontal(
            text: String,
            rect: RectF,
            canvas: Canvas,
        ) {
            textPaint.textSize = rect.height()
            canvas.drawText(text, rect.left, rect.top + rect.height(), textPaint)
        }

        private fun drawTextVertical(
            text: String,
            rotation: Float,
            rect: RectF,
            canvas: Canvas,
        ) {
            canvas.save()
            canvas.rotate(rotation, rect.left, rect.top)
            canvas.drawText(text, rect.left, rect.top, textPaint)
            canvas.restore()
        }

        private fun isHorizontal(rect: RectF): Boolean {
            return rect.height() < rect.width()
        }

        companion object {
            private const val TEXT_COLOR = Color.BLACK
            private const val MARKER_COLOR = Color.GRAY
            private const val STROKE_WIDTH = 4.0f
            private const val ROTATION = 90F
        }
    }
