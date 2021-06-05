package com.bignerdranch.drawcanvas

import android.content.Context
import android.graphics.*
import android.view.View

class CanvasView(context: Context) : View(context) {

    private val paint = Paint().apply {
        color = 0xff2040ff.toInt()
    }
    private val path = Path().apply {
        moveTo(10f, 0f)
        lineTo(320f, -150f)
        lineTo(620f, 0f)
        lineTo(320f, 150f)
        lineTo(20f,0f)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.apply {
            paint.color = Color.BLUE

            translate(900f, 540f)
            rotate(45f)
            drawPath(path, paint)

//            scale(-1f,1f)
//            drawPath(path, paint)

            rotate(90f)
            drawPath(path, paint)

            rotate(90f)
            drawPath(path, paint)

            rotate(90f)
            drawPath(path, paint)
//
//            scale(-1f,1f)
//            drawPath(path, paint)
        }
    }
}