package com.bignerdranch.squaretarget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory.decodeResource
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow

const val TAG = "GameView"

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var w = 0;
    private var h = 0   // 화면의 크기
    private var cx = 0;
    private var cy = 0 // 화면의 중심

    private var backgroundImg: Bitmap? = null
    private var target: Bitmap? = null

    private var tw = 0

    private var score = 0  // 현재 점수
    private var total = 0  // 누적 점수

    private val rect = mutableListOf<RectF>()
    private val mHole = mutableListOf<BulletHole>()

    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        this.cx = w / 2
        this.cy = h / 2
        getBitmap()
        getRect()
    }

    private fun getBitmap() {
        val decodeBitmap = decodeResource(resources, R.drawable.background)
        backgroundImg = createScaledBitmap(decodeBitmap, w, h, true)

        val targetBitmap = decodeResource(resources, R.drawable.target)
        tw = targetBitmap.width
        Log.d(TAG, "${targetBitmap.width}")
        target = createScaledBitmap(targetBitmap, (targetBitmap.width * 0.8).toInt(), (targetBitmap.height * 0.8).toInt(), true)
    }

    private fun getRect() {
        //화면 확대 비율
        val r = (tw * 0.8) / 280f
        // 픽셀 * 비율 * 0.5 - 0.5 를 곱해주는 이유는 중심을 제대로 잡기 위해서.
        val size = listOf(280 * r * 0.5f, 180 * r * 0.5f, 80 * r * 0.5f)

        // 사각형의 좌표를 각각 구한다.
        (0 until 3).forEach {
            rect.add(RectF((cx - size[it]).toFloat(),
                    (cy - size[it]).toFloat(),
                    (cx + size[it]).toFloat(),
                    (cy + size[it]).toFloat()))
        }
    }

    // 점수 판정
    private fun checkScore(x: Float, y: Float): Int {
        // 각 사각형의 점수
        val n = listOf(6, 8, 10)
        score = 0

        // 작은 사각형 부터 판정 왜냐하면
        // listOf 에서 작은 거부터 나오게함.
        for (i in 2 downTo 0) {
            if (rect[i].contains(x, y)) {
                score = n[i]
                total += score
                break;
            }
        }
        return score
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        backgroundImg?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        target?.let {
            canvas.drawBitmap(it, cx - (it.width / 2f), cy - (it.height / 2f), null)
        }

        mHole.forEach {
            canvas.drawBitmap(it.hole, it.x - it.w, it.y - it.h, null)
        }

        val str1 = "득점 : $score"
        val str2 = "총점 : $total"
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(str1, 100f, 100f, paint)

        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(str2, w - 100f, 100f, paint)
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y

            if (checkScore(x, y) > 0) {
                mHole += BulletHole(x, y)
            }
        }
        invalidate()
        return true
    }

    inner class BulletHole(val x: Float, val y: Float) {
        var hole: Bitmap
        var w: Float
        var h: Float

        init {
            hole = decodeResource(resources, R.drawable.tan)
            w = hole.width / 2f
            h = hole.height / 2f
        }
    }
}