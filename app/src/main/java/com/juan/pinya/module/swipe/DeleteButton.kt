package com.juan.pinya.module.swipe

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.juan.pinya.model.DailyReport

class DeleteButton(
    private val context: Context,
    private val text: String,
    private val textSize: Int,
    private val imageResId: Int,
    private val color: Int,
    private val listener: () -> Unit
) {
    private var pos: Int = 0
    private var clickRegion: RectF? = null

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion?.contains(x, y) == true) {
            listener.invoke()
            return true
        }
        return false
    }

    fun onDraw(canvas: Canvas, rectf: RectF, pos: Int) {
        val paint = Paint()
        paint.color = color
        canvas.drawRect(rectf, paint)

        paint.color = Color.WHITE
        paint.textSize = textSize.toFloat()

        val rect = Rect()
        val cHeight = rectf.height()
        val cWidth = rectf.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, rect)
        if (imageResId == 0) {
            val x = cWidth / 2f - rect.width() / 2f - rect.left.toFloat()
            val y = cHeight / 2f + rect.height() / 2f - rect.bottom
            canvas.drawText(text, rectf.left + x, rectf.top + y, paint)
        } else {
            val drawable = ContextCompat.getDrawable(context, imageResId) ?: return
            val bitmap = drawableToBitmap(drawable)
            canvas.drawBitmap(bitmap,
                (rectf.left + rectf.right) / 2,
                (rectf.top + rectf.bottom) / 2,
                paint)
        }
        clickRegion = rectf
        this.pos = pos

    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.toBitmap()
        }
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}