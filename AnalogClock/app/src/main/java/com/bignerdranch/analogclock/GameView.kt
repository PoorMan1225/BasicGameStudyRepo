package com.bignerdranch.analogclock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

const val TAG = "GameView"

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var cx = 0
    private var cy = 0

    private var hour = 0; private var rHour = 0f
    private var min = 0; private var rMinute = 0f
    private var second = 0; private var rSecond = 0f

    private val clock:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.clock)

    private val pin: List<Bitmap> = listOf(
        BitmapFactory.decodeResource(resources, R.drawable.si),
        BitmapFactory.decodeResource(resources, R.drawable.boon),
        BitmapFactory.decodeResource(resources, R.drawable.cho)
    )

    private val repeatAction = object : Runnable {
        override fun run() {
            getTime()
            invalidate()
            handler?.postDelayed(this, 100L) // 1초
        }
    }

    private val cw  = clock.width / 2

    private val pw:List<Int> = listOf(
        pin[0].width / 2,
        pin[1].width / 2,
        pin[2].width / 2
    )

    private val ph:List<Int> = listOf(
        pin[0].height,
        pin[1].height,
        pin[2].height
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2
        cy = h / 2
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.scale(0.9f, 0.9f, cx.toFloat(), cy.toFloat())
        canvas.save()
        canvas.drawBitmap(clock, (cx-cw).toFloat(), (cy-cw).toFloat(), null)

        //시침
        canvas.rotate(rHour, pw[0].toFloat(), ph[0].toFloat())
        canvas.drawBitmap(pin[0], (cx-pw[0]).toFloat(), (cy-ph[0]).toFloat(), null)

        //분침
        canvas.rotate(rMinute - rHour, pw[1].toFloat(), ph[1].toFloat()) // rHour 의  canvas의 회전값을 고려해서 빼준다.
        canvas.drawBitmap(pin[1], (cx-pw[1]).toFloat(), (cy-ph[1]).toFloat(), null)

        //초침
        canvas.rotate(rSecond-rMinute, pw[2].toFloat(), ph[2].toFloat())
        canvas.drawBitmap(pin[2], (cx-pw[2]).toFloat(), (cy-ph[2]).toFloat(), null)

    }

    private fun getTime() {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+9"))

        hour = calendar.get(Calendar.HOUR)
        min = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)

        rSecond = second * 6f
        rMinute = (min * 6f) + rSecond / 60f
        rHour = (hour * 30f) + rMinute / 12f
        Log.d(TAG, "second: $rSecond, hour: $rHour, minute: $rMinute")
    }

    fun startHandler() {
        handler?.postDelayed(repeatAction, 100L)
    }

    fun stopHandler() {
        handler?.removeCallbacks(repeatAction)
    }
}