package com.rjhwork.mycompany.bounceball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.Exception
import java.util.*

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private lateinit var imgBack: Bitmap
    private var w: Int = 0
    private var h: Int = 0

    val balls = Collections.synchronizedList(mutableListOf<BallClass>())

    private lateinit var thread: GameThread

    init {
        CommonResources.set(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        imgBack = BitmapFactory.decodeResource(resources, R.drawable.field)
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true)

        thread = GameThread().apply {
            start()
        }
    }

    private fun makeBall(px: Float, py: Float) {
        synchronized(balls) {
            balls.add(BallClass(w, h, px, py))
        }
    }

    private fun moveBall() {
        synchronized(balls) {
            balls.forEach {
                it.update()
            }
        }
    }

    private fun removeBall() {
        synchronized(balls) {
            for (i in balls.size - 1 downTo 0) {
                if (balls[i].isDead)
                    balls.removeAt(i)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(imgBack, 0f, 0f, null)

        synchronized(balls) {
            balls.forEach {
                canvas.rotate(it.ang, it.px, it.py)
                canvas.drawBitmap(it.ball, it.px - it.r, it.py - it.r, null) // 그려주는 부분만 좌표 설정
                canvas.rotate(-it.ang, it.px, it.py)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            makeBall(event.x, event.y)
        }
        return true
    }

    override fun onDetachedFromWindow() {
        thread.isRun = false
        super.onDetachedFromWindow()
    }

    inner class GameThread : Thread() {
        var isRun = true

        override fun run() {
            while (isRun) {
                try {
                    Time.update()

                    moveBall()
                    removeBall()
                    postInvalidate()
                    sleep(10) // 최대 fps 를 100으로 유지하기 위함.
                } catch (e: Exception) {

                }
            }
        }
    }
}