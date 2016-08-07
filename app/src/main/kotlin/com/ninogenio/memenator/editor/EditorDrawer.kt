package com.ninogenio.memenator.editor

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import org.jetbrains.anko.windowManager
import rx.Observable
import java.util.*

class EditorDrawer(private var bitmap: Bitmap, context: Context) {

    init {
        // Scale down the bitmap image
        val metrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metrics)
        val displayWidth = metrics.widthPixels.toFloat()
        val imageWidth = bitmap.width.toFloat()
        val imageHeight = bitmap.height.toFloat()
        val aspectRatio: Float = imageWidth / imageHeight
        val finalWidth: Float
        if (imageWidth > displayWidth) {
            finalWidth = displayWidth
        } else {
            finalWidth = imageWidth
        }
        val finalHeight = finalWidth / aspectRatio

        bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth.toInt(), finalHeight.toInt(), true)
    }

    fun generateBitmap(topText: String, bottomText: String) = Observable.create<Bitmap> { subscriber ->
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textSize = 45f
        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        val textPaintOutline = Paint()
        textPaintOutline.isAntiAlias = true
        textPaintOutline.textSize = 45f
        textPaintOutline.color = Color.BLACK
        textPaintOutline.style = Paint.Style.STROKE
        textPaintOutline.strokeWidth = 4.toFloat()

        val alteredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(alteredBitmap)
        canvas.drawBitmap(bitmap, 0.toFloat(), 0.toFloat(), Paint())

        // Write text on top
        writeOnTop(topText, textPaintOutline, canvas)
        writeOnTop(topText, textPaint, canvas)

        // Write text on bottom
        writeOnBottom(bottomText, textPaintOutline, canvas)
        writeOnBottom(bottomText, textPaint, canvas)

        subscriber.onNext(alteredBitmap)
    }

    private fun writeOnTop(text: String, paint: Paint, canvas: Canvas) {
        var wholeText = text
        val lines = ArrayList<String>()
        while (!wholeText.isEmpty()) {
            val length = paint.breakText(wholeText, true, (canvas.width - 3).toFloat(), null)
            lines.add(wholeText.substring(0, length))
            wholeText = wholeText.substring(length)
        }

        // Set position
        val bounds = Rect()
        var yoff = 3
        for (line in lines) {
            canvas.drawText(line, xBound(paint.measureText(text)), 45 + yoff.toFloat(), paint)
            paint.getTextBounds(line, 0, line.length, bounds)
            yoff = bounds.height() + yoff + 10
        }
    }

    private fun writeOnBottom(text: String, paint: Paint, canvas: Canvas) {
        var wholeText = text
        val lines = ArrayList<String>()
        while (!wholeText.isEmpty()) {
            val length = paint.breakText(wholeText, true, (canvas.width - 3).toFloat(), null)
            lines.add(wholeText.substring(0, length))
            wholeText = wholeText.substring(length)
        }

        // Set position
        val bounds = Rect()
        val yoff = canvas.height - 10
        var ySet = 1.toFloat()
        for (line in lines) {
            val yCordinate = yoff - ((lines.size - ySet) * 45)
            canvas.drawText(line, xBound(paint.measureText(text)), yCordinate, paint)
            paint.getTextBounds(line, 0, line.length, bounds)
            ySet++
        }
    }

    private fun xBound(textWidth: Float): Float {
        val width = bitmap.width
        return ((width - textWidth) / 2).toFloat()
    }

}