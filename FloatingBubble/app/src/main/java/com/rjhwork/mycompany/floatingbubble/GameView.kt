package com.rjhwork.mycompany.floatingbubble

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

class GameView(context: Context, attrs:AttributeSet? = null): View(context, attrs) {
    private lateinit var imgBack: Bitmap
    private val mBubble = mutableListOf<Bubble>()

    // 화면 크기.
    private var w:Int = 0
    private var h:Int = 0

    private val repeatAction = object :Runnable {
        override fun run() {
            moveBubble()
            invalidate()
            handler?.postDelayed(this, 10)
        }
    }

    /**
     * 배경 이미지는 onSizeChanged() 에서 만드는데, onSizeChanged()는 생성자보다
     * 늦게 실행되므로 핸들러를 너무 빨리 기동해서는 안된다. 핸들러가 기동되면 자동으로
     * onDraw() 함수가 실행되는데, 배경 이미지를 미처 만들지 않은 상태에서 배경 그리기를
     * 시도하면 NullPointException 에러가 발생한다.
     *
     * 핸들러가 너무 빨리 기동되어 생기는 문제를 근본적으로 해결하려면, 생성자가 아니라
     * onSizeChanged() 에서 핸들러를 기동한다. 이 방법을 사용할 경우에는 생성자에서 핸들러를
     * 호출하는 문장은 삭제한다.
     */

    /**
     * 생성자는 GameView 가 만들어질때 단 한번 호출되지만, onSizeChanged() 함수는 View
     * 의 크기가 바뀔 때 마다 호출된다. MainActivity 는 View 를 만든 후에 타이틀 바를 추가하므로
     * View 가 만들어질때 한번, 타이틀 바가 추가 될때 이함수가 다시 호출될 것이다. 물론 전체
     * 화면을 사용하면 한 번 호출된다.
     */
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
            canvas.drawBitmap(it.bubble, (it.x - it.bw/2).toFloat(), (it.y - it.bw/2).toFloat(), null)
        }
    }

    private fun moveBubble() {
        mBubble.forEach {
            it.update()
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
            val x = event.x.toInt()
            val y = event.y.toInt()

            mBubble.add(Bubble(context, w, h, x, y))
        }

        return super.onTouchEvent(event)
    }
}