package com.ninogenio.memenator.editor

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import org.jetbrains.anko.windowManager
import rx.Observable
import java.util.*

/*
 * Some codes here referred from this open-sourced project: http://androidhelpandtips.blogspot.co.id/2016/02/meme-generator-android-app-source-code.html
 */
class EditorDrawer(private var bitmap: Bitmap, private val context: Context) {

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

    var textSize = 45f

    fun generateBitmap(topText: String, bottomText: String) = Observable.create<Bitmap> { subscriber ->
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/impact.ttf")
        textPaint.textSize = textSize
        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        val textPaintOutline = Paint()
        textPaintOutline.isAntiAlias = true
        textPaintOutline.typeface = Typeface.createFromAsset(context.assets, "fonts/impact.ttf")
        textPaintOutline.textSize = textSize
        textPaintOutline.color = Color.BLACK
        textPaintOutline.style = Paint.Style.STROKE
        textPaintOutline.strokeWidth = 10.toFloat()

        val alteredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(alteredBitmap)
        canvas.drawBitmap(bitmap, 0.toFloat(), 0.toFloat(), Paint())

        // Write text on top
        writeOnTop(topText,
                breakTextToLines(topText, textPaintOutline, canvas),
                textPaintOutline,
                textPaint,
                canvas)

        // Write text on bottom
        writeOnBottom(bottomText,
                breakTextToLines(bottomText, textPaintOutline, canvas),
                textPaintOutline,
                textPaint,
                canvas)

        subscriber.onNext(alteredBitmap)
    }

    private fun breakTextToLines(text: String, paint: Paint, canvas: Canvas): List<String> {
        var wholeText = text
        val lines = ArrayList<String>()
        while (!wholeText.isEmpty()) {
            val length = paint.breakText(wholeText, true, (canvas.width - 3).toFloat(), null)
            lines.add(wholeText.substring(0, length))
            wholeText = wholeText.substring(length)
        }
        return lines
    }

    private fun writeOnTop(text: String, lines: List<String>, textPaintOutline: Paint, textPaint: Paint, canvas: Canvas) {
        // Set position
        val bounds = Rect()
        var yoff = 3
        for (line in lines) {
            canvas.drawText(line, xBound(textPaintOutline.measureText(text)), textSize + yoff.toFloat(), textPaintOutline)
            canvas.drawText(line, xBound(textPaint.measureText(text)), textSize + yoff.toFloat(), textPaint)
            textPaintOutline.getTextBounds(line, 0, line.length, bounds)
            yoff = bounds.height() + yoff + 10
        }
    }

    private fun writeOnBottom(text: String, lines: List<String>, textPaintOutline: Paint, textPaint: Paint, canvas: Canvas) {
        // Set position
        val bounds = Rect()
        val yoff = canvas.height - 10
        var ySet = 1.toFloat()
        for (line in lines) {
            val yCordinate = yoff - ((lines.size - ySet) * textSize)
            canvas.drawText(line, xBound(textPaintOutline.measureText(text)), yCordinate, textPaintOutline)
            canvas.drawText(line, xBound(textPaint.measureText(text)), yCordinate, textPaint)
            textPaintOutline.getTextBounds(line, 0, line.length, bounds)
            ySet++
        }
    }

    private fun xBound(textWidth: Float): Float {
        val width = bitmap.width
        return ((width - textWidth) / 2).toFloat()
    }

}