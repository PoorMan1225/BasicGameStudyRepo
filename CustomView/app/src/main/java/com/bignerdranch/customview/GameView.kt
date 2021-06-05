package com.bignerdranch.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context:Context, attrs:AttributeSet? = null) : View(context, attrs) {

    private val imgRes = listOf(R.drawable.cat1, R.drawable.cat2)
    private var cats = mutableListOf<Bitmap>(
        BitmapFactory.decodeResource(resources, imgRes[0]),
        BitmapFactory.decodeResource(resources, imgRes[1])
    )

    private var w:Int = 0 ; private var h:Int = 0       // 화면의 크기
    private var x1:Float = 300f; private var y1:Float = 200F       // 토끼의 좌표
    private var rw:Int = 0; private var rh:Int = 0      // 토끼 이미지의 크기의 절반.
    private var sx:Int = 3; private var sy:Int = 2      // 토끼의 이동속도.

    private var counter = 0
    private var imgNum = 0

    val repeatAction:Runnable = object :Runnable {
        override fun run() {
            invalidate()
            handler?.postDelayed(this, 10L)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        initResource(w, h)
    }

    private fun initResource(w: Int, h: Int) {

        for(i in 0 until cats.size) {
            cats[i] = Bitmap.createScaledBitmap(cats[i], w / 6, h / 3, true)
        }
        rw = cats[0].width / 2
        rh = cats[0].height / 2
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if(event.action == MotionEvent.ACTION_DOWN) {
            x1 = event.x
            y1 = event.y
        }

        if(event.action == MotionEvent.ACTION_UP) {
            val x2 = event.x
            val y2 = event.y

            sx = ((x2 - x1) / 100).toInt()
            sy = ((y2 - y1) / 100).toInt()
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        x1 += sx
//        if(x < rw || x > w - rw) {
//            sx = -sx
//        }

        y1 += sy
//        if(y < rh || y > h - rh) {
//            sy = -sy
//        }

        counter++
        if(counter % 50 == 0) {
            imgNum = 1 - imgNum
        }
        canvas.drawBitmap(cats[imgNum], (x1 - rw), (y1 - rh), null)
    }
}