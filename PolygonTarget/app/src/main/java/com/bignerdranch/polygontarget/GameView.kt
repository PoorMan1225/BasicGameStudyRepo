package com.bignerdranch.polygontarget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.pow

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

    private val pt = mutableListOf<MutableList<Point>>(
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        this.cx = w / 2
        this.cy = h / 2
        getBitmap()
        getArea()
    }

    private fun getBitmap() {
        val decodeBitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
        backgroundImg = Bitmap.createScaledBitmap(decodeBitmap, w, h, true)

        val targetBitmap = BitmapFactory.decodeResource(resources, R.drawable.polygon)
        tw = targetBitmap.width / 2
        target = Bitmap.createScaledBitmap(
            targetBitmap,
            (targetBitmap.width).toInt(),
            (targetBitmap.height).toInt(),
            true
        )
    }

    private fun getArea() {

        val org = listOf(140, 90, 40)
        // 반지름에 대한 비율이므로 반을 나눠야 한다.
        val r = (tw) / 140f

        (0..2).forEach { i ->
            radius.add(org[i] * r)

            // 꼭지점 좌표 계산.
            (0 until 6).forEach { j ->
                // 여기서 0부터 시작해야 맨 오른쪽 꼭짓점 좌표 값나옴.
                val rad = Math.toRadians((60 * j).toDouble())
                val x = cx + Math.cos(rad) * radius[i]
                val y = cy - Math.sin(rad) * radius[i]

                pt[i].add(j, Point(x.toInt(), y.toInt()))
            }
            // 마지막 점은 시작점.
            pt[i].add(6, Point(pt[i][0]))
        }
    }

    // 점수 판정
    private fun checkScore(x: Float, y: Float): Int {
        // 각 섹터의 점수
        val n = listOf(10, 4, 10, 6, 4, 8, 10)

        // 각도 계산
        var deg =
            (-Math.toDegrees(atan2(y - cy,x - cx).toDouble()))
        if (deg < 0) deg += 360
        score = 0

        // degree 를 60으로 나눠야 idx 가 나온다.
        val idx = (deg / 60).toInt()
        // 작은 사각형 부터 판정 왜냐하면
        // listOf 에서 작은 거부터 나오게함.
        for (i in 2 downTo 0) {
            var count = 0

            (0 until 6).forEach { j ->
                if (hitTest(x, y, pt[i][j], pt[i][j + 1])) {
                    count++
                }
            }

            if (count % 2 == 1) {
                score = n[idx] * (i + 1)
                total += score
                break
            }
        }
        return score
    }

    // 타격 지점 확인.
    private fun hitTest(x: Float, y: Float, p1: Point, p2: Point): Boolean {
        var hit = false

        // 점이 선분과 교차하는가?
        if ((y > p1.y && y <= p2.y) || (y < p1.y && y >= p2.y)) {
            // 기울기.
            // 기울기 구할때, 분자 또는 분모를 float값으로 캐스팅 해줘야
            // 정확한 값으로 나옴 왜냐하면 이게 point 의 x, y 값이 int이기 때문에
            // 그냥 나누게 되면 정확한 기울기가 나오지 않음.
            val m = (p2.y - p1.y) / (p2.x - p1.x).toFloat()
            val px = p1.x + (y - p1.y) / m

            if (x < px) {
                hit = true
            }
        }
        return hit
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
        val dart: Bitmap
        var w: Float
        var h: Float

        init {
            dart = BitmapFactory.decodeResource(resources, R.drawable.dart)
            w = 0f
            h = dart.height.toFloat()
        }
    }
}