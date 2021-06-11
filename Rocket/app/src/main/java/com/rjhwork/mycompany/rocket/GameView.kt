package com.rjhwork.mycompany.rocket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {

    private lateinit var imgBack: Bitmap
    private lateinit var rocket:Rocket

    private var isLaunch = true
    private var sw:Int = 0
    private var sh:Int = 0

    private lateinit var thread:GameThread

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.sw = w
        this.sh = h

        imgBack = BitmapFactory.decodeResource(resources, R.drawable.sky)
        imgBack = Bitmap.createScaledBitmap(imgBack, sw, sh, true)

        rocket = Rocket(context, sw, sh)

        thread = GameThread().apply {
            start()
        }
    }

    inner class GameThread : Thread() {
        var canRun = true

        override fun run() {
            while (canRun) {
                try{
                    Time.update()

                    moveRocket()
                    postInvalidate()
                    sleep(10)
                }catch (e: Exception) {

                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(imgBack, 0f, 0f, null)

        canvas.rotate(rocket.ang, rocket.x, rocket.y)
        canvas.drawBitmap(rocket.rocket, rocket.x- rocket.w, rocket.y - rocket.h, null)
        canvas.rotate(-rocket.ang, rocket.x, rocket.y)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if(!isLaunch && event.action == MotionEvent.ACTION_DOWN) {
            rocket.launch(event.x, event.y)
            isLaunch = true
        }
        return true
    }

    override fun onDetachedFromWindow() {
        thread.canRun = false
        super.onDetachedFromWindow()
    }

    private fun moveRocket() {
        if(isLaunch) {
            isLaunch = rocket.update()
        }
    }
}