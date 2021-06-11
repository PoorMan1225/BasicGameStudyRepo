package com.rjhwork.mycompany.floatingbubble3t

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.Exception
import java.util.*

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private lateinit var imgBack: Bitmap
    private val mBubble = Collections.synchronizedList(mutableListOf<Bubble>())
    private val rnd = Random()
    private val paint = Paint()
    private lateinit var mThread: GameThread

    // 화면 크기.
    private var w: Int = 0
    private var h: Int = 0

    inner class GameThread : Thread() {
        var canRun: Boolean = true

        override fun run() {
            while (canRun) {
                try {
                    Time.update()

                    makeBubble()
                    moveBubble()
                    removeDead()
                    postInvalidate()
                    sleep(10)
                }catch (e:Exception) {

                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        imgBack = BitmapFactory.decodeResource(resources, R.drawable.sky)
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true)

        // 스레드를 사용한 결과, 비눗방울이 좀더 자연스럽게 이동하지만,
        // 스레드의 루프가 고속으로 수행되므로 비눗방울 20개가 동시에 화면에
        // 나타나는 문제가 있다. 이것을 makeBubble() 함수의 비눗 방울
        // 발생 빈도를 조절하는 것으로 해결한다.
        mThread = GameThread().apply {
            start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(imgBack, 0f, 0f, null)

        synchronized(mBubble) {
            mBubble.forEach {
                canvas.drawBitmap(it.bubble, (it.x - it.r), (it.y - it.r), null)
            }
        }

        synchronized(mSmall) {
            mSmall.forEach {
                paint.alpha = it.alpha
                canvas.drawBitmap(it.bubble, it.px - it.r, it.py - it.r, paint)
            }
        }
    }

    /**
     * 스레드를 지연 처리하도록 수정하였으므로 이전의
     * rnd.nextInt(2000) < 1 코드는 지우고 원상태로 이전한다.
     */
    private fun makeBubble() {
        synchronized(mBubble) {
            if (mBubble.size < 20 && rnd.nextInt(1000) < 8) {
                mBubble.add(Bubble(context, w, h))
            }
        }
    }

    // 비눗 방울 이동.
    private fun moveBubble() {
        synchronized(mBubble) {
            mBubble.forEach {
                it.update()
            }
        }

        // 파편의 이동.
        synchronized(mSmall) {
            mSmall.forEach {
                it.update()
            }
        }
    }

    private fun hitTest(x: Float, y: Float) {
        synchronized(mBubble) {
            mBubble.forEach { bubble ->
                if (bubble.hitTest(x, y)) {
                    return
                }
            }
        }
    }

    // 죽었다 표시한 것만 따로 돌면서 사라짐.
    private fun removeDead() {
        synchronized(mBubble) {
            for (i in mBubble.size - 1 downTo 0) {
                if (mBubble[i].isDead) {
                    mBubble.removeAt(i)
                }
            }
        }

        // 파편 제거
        synchronized(mSmall) {
            for (i in mSmall.size - 1 downTo 0) {
                if (mSmall[i].isDead) {
                    mSmall.removeAt(i)
                }
            }
        }
    }

    // onDetachedFromWindow() View의 실행이 종료될 때 호출되는 콜백 함수이다.
    override fun onDetachedFromWindow() {
        mThread.canRun = false
        super.onDetachedFromWindow()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            hitTest(event.x, event.y)
        }
        return true
    }

    companion object {
        // 비눗방울의 파편은 GameView 에 저장해야 하므로
        // SmallBubble을 GameView 에 리스트로 만든다.
        // 스레드에 안전한 리스트 의 형태.
        val mSmall: MutableList<SmallBubble> = Collections.synchronizedList(mutableListOf<SmallBubble>())
    }
}