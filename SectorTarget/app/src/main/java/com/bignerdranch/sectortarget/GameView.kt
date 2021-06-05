package com.bignerdranch.sectortarget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
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

    private val radius = mutableListOf<Float>() // 원의 반지름.
    private val mHole = mutableListOf<BulletDart>()

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
        getRadius()
    }

    private fun getBitmap() {
        val decodeBitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
        backgroundImg = Bitmap.createScaledBitmap(decodeBitmap, w, h, true)

        val targetBitmap = BitmapFactory.decodeResource(resources, R.drawable.target)
        tw = targetBitmap.width
        Log.d(TAG, "$tw")
        target = Bitmap.createScaledBitmap(
            targetBitmap,
            (targetBitmap.width * 0.8f).toInt(),
            (targetBitmap.height * 0.8f).toInt(),
            true
        )
    }

    private fun getRadius() {

        val org = listOf(140, 90, 40)
        // 반지름에 대한 비율이므로 반을 나눠야 한다.
        val r = (tw/2) / 140f

        (0..2).forEach {
            radius.add(org[it] * r * 0.8f)
        }
    }

    // 점수 판정
    private fun checkScore(x: Float, y: Float): Int {
        // 각 섹터의 점수
        val n = listOf(10, 6, 12, 4, 15, 8, 10, 6, 12, 4, 15, 6)

        // 각도 계산
        // 이 삼각형의 꼭지각 세타를 구하면 x, y 가 수평선으로부터 시계 반대 방향으로 몇 도 회전했는지 알 수 있다.
        // (원내부에서 어떤 구역에 있는지 판단하기 위함)
        var deg = (-Math.toDegrees(atan2(y = (y - cy).toDouble(), x = (x - cx).toDouble()))).toFloat()
        Log.d(TAG, "deg: ${deg.toString()}")
        if (deg < 0) deg += 360f
        score = 0

        // 살짝 15도로 회전을 해서 하므로 deg 에서 15를 더한다.
        // 맨 왼쪽이 아래로가면 -180도로 시작하게 되므로
        // 360을 더해야 아래가 180으로 계산되면서 끝으로 돌면 360으로 계산된다.
        // 그렇기 때문에 마지막이 360 도로 계산되면서 / 30 으로 하면 인덱스가
        // 계산된다.
        val idx = (deg + 15) % 360 / 30
        // 작은 사각형 부터 판정 왜냐하면
        // listOf 에서 작은 거부터 나오게함.
        for (i in 2 downTo 0) {
            if ((cx - x).toDouble().pow(2.0) + (cy - y).toDouble().pow(2.0) < (radius[i] * radius[i])) {
                // 안쪽 부터  점수의 두배 처리. 
                score = n[idx.toInt()] * (i + 1)
                total += score
                break
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
            canvas.drawBitmap(it.dart, it.x, it.y - it.h, null)
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
                mHole += BulletDart(x, y)
            }
        }
        invalidate()
        return true
    }

    inner class BulletDart(val x: Float, val y: Float) {
        var dart: Bitmap
        var w: Float
        var h: Float

        init {
            dart = BitmapFactory.decodeResource(resources, R.drawable.dart)
            w = 0f
            h = dart.height.toFloat()
        }
    }
}