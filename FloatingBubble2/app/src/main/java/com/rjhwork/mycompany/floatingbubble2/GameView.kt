package com.rjhwork.mycompany.floatingbubble2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class GameView(context: Context, attrs:AttributeSet? = null): View(context, attrs) {
    private lateinit var imgBack: Bitmap
    private val mBubble = mutableListOf<Bubble>()
    private val rnd = Random()

    // 화면 크기.
    private var w:Int = 0
    private var h:Int = 0

    private val repeatAction = object :Runnable {
        override fun run() {
            makeBubble()
            moveBubble()
            invalidate()
            handler?.postDelayed(this, 10)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        imgBack = BitmapFactory.decodeResource(resources, R.drawable.sky)
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(imgBack, 0f, 0f, null)

        mBubble.forEach {
            canvas.drawBitmap(it.bubble, (it.x - it.r), (it.y - it.r), null)
        }
    }

    // 비눗 방울 수를 20개 이내로 제한 하며, 매 프레임마다
    // 8/1000의 확률로 나타나게 한다. 이함수는 핸들러가 호출한다.
    private fun makeBubble() {
        if(mBubble.size < 20 && rnd.nextInt(1000) < 8) {
            mBubble.add(Bubble(context, w, h))
        }
    }

    // 비눗 방울 이동.
    private fun moveBubble() {
        mBubble.forEach {
            it.update()
        }
    }

    // Touch Event 로 부터 터치 좌표를 전달받아 모든 비눗방울을 조사한다.
    // 터치 위에 있는 비눗 방울은 최초로 발견된 것만 제거한다. return 이 없으면
    // 여러개의 데이터가 삭제 될수 있고 인덱스가 뒤로 밀리면서 에러가 발생할 수 있다.
    private fun hitTest(x:Float, y:Float) {
        mBubble.forEach { bubble ->
            if(bubble.hitTest(x, y)) {
                mBubble.remove(bubble)
                return
            }
        }
    }

    // customView 액티비티에 attach 될때 바로 핸들러 호출.
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed(repeatAction, 100)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if(event.action == MotionEvent.ACTION_DOWN) {
            hitTest(event.x, event.y)
        }
        return true
    }
}