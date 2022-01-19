package com.alizaidi.busroute.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import java.lang.Exception
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import android.graphics.PorterDuff

import androidx.core.content.ContextCompat

import android.graphics.PorterDuffColorFilter

import android.graphics.ColorFilter
import com.alizaidi.busroute.R


class MapUtils() {

    fun GetBitmapMarker(mContext: Context, resourceId: Int, mText: String): Bitmap? {
        return try {
            val resources: Resources = mContext.resources
            val scale: Float = resources.displayMetrics.density
            var bitmap = BitmapFactory.decodeResource(resources, resourceId)
            var bitmapConfig = bitmap.config

            // set default bitmap config if none
            if (bitmapConfig == null) bitmapConfig = Bitmap.Config.ARGB_8888
            bitmap = bitmap.copy(bitmapConfig, true)

            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.setColor(Color.WHITE)
            paint.setTextSize((12 * scale).toInt().toFloat())
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY)

            // draw text to the Canvas center
            val bounds = Rect()
            paint.getTextBounds(mText, 0, mText.length, bounds)
            val x: Int = (bitmap.width - bounds.width()) / 2
            val y: Int = (bitmap.height + bounds.height()) /2
            canvas.drawText(mText, x * scale, y * scale-5, paint)
            // Create new bitmap based on the size and config of the old
            bitmap
        } catch (e: Exception) {
            null
        }
    }
}