package com.rjhwork.mycompany.huntingbird2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class CommonResources {

    companion object {
        val listBirds = mutableListOf<Bitmap>()
        var bw:Int = 0
        var bh:Int = 0

        fun set(context: Context) {
            val org = BitmapFactory.decodeResource(context.resources, R.drawable.sparrow)

            bw = org.width / 6
            bh = org.height

            (0..5).forEach { i ->
                // 0,0 부터 이미지를 짜르기 때문에 y = 0 가된다.
                listBirds.add(Bitmap.createBitmap(org, bw*i, 0, bw, bh))
            }

            // 이미지의 폭과 높이(1/2)
            bw /= 2
            bh /= 2
        }
    }
}