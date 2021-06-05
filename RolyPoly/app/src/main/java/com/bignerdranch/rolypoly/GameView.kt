package com.bignerdranch.rolypoly

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    // 배경
    private var backgroundImg:Bitmap? = null

    // 개
    private val dogImg: Bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.dog)
    }

    // 그림자
    private val shadowImg: Bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.shadow)
    }

    private var w = 0; private var h = 0       // 넓이, 높이
    private var cx = 0; private var cy = 0     // 가운데 좌표

    // 회전 각도, 이동방향, 좌우 한계
    private var ang = 0; private var dir = 0
    private var lLimit = -15; private var rLimit = 15

    private val repeatAction = object : Runnable {
        override fun run() {
            invalidate()
            handler?.postDelayed(this, 10L)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w   // 화면의 폭
        this.h = h   // 화면의 넓이

        this.cx = w / 2
        this.cy = h / 2

        // 배경 이미지 확대.
        backgroundImg = decodeScale(resources, R.drawable.background, w, h, true)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.apply {
            backgroundImg?.let {
                drawBitmap(it, 0f, 0f, null)
            }
            drawBitmap(shadowImg, cx-(shadowImg.width/2f), (cy-(shadowImg.height/2f)+(dogImg.height/4f)), null)
            RotateDog()
            rotate(ang.toFloat(), cx.toFloat(), cy.toFloat())
            drawBitmap(dogImg, cx-(dogImg.width/2f), cy-(dogImg.height/2f), null)
            rotate(-ang.toFloat(), cx.toFloat(), cy.toFloat())
        }
    }

    private fun decodeScale(resource: Resources, drawable:Int, w:Int, h:Int, filter:Boolean):Bitmap {
        var bitmap = BitmapFactory.decodeResource(resource, drawable)
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, filter)
        return bitmap
    }

    private fun RotateDog() {
        ang += dir

        if(ang <= lLimit || ang >= rLimit) {
            lLimit++
            rLimit--
            dir = -dir
            ang += dir
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(event.action == MotionEvent.ACTION_DOWN) {
            lLimit = -15
            rLimit = 15

            if(dir == 0) {
                dir = -1
            }
        }
        return true
    }

    fun startAnimate() {
        handler?.post(repeatAction)
    }

    fun stopAnimate() {
        handler?.removeCallbacks(repeatAction)
    }
}

