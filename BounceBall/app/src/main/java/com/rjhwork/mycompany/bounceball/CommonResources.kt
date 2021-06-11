package com.rjhwork.mycompany.bounceball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class CommonResources {

    companion object {
        lateinit var ball: Bitmap
        var r = 80

        fun set(context: Context) {
            ball = BitmapFactory.decodeResource(context.resources, R.drawable.ball)
            ball = Bitmap.createScaledBitmap(ball, r * 2, r * 2, true)
        }
    }
}