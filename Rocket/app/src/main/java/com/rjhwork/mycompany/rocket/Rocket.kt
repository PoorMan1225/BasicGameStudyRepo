package com.rjhwork.mycompany.rocket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Rocket(private val context: Context,
             val sw:Int,
             val sh:Int,
             ) {

    private val speed = 1000     // 이동
    private val gravity = 400f      // 중력
    private val dir = PointF()      // 이동 방향
    var w:Int               // 로켓 크기의 절반
    var h:Int               // 로켓 높이의 절반.
    var x:Float = 0f             // 로켓 위치
    var y:Float = 0f             // 로켓 위치
    var ang:Float = 0f

    var rocket: Bitmap

    init {
        // 이미지를 원본 크기로 표시할 경우 이렇게 사용.
        val option = BitmapFactory.Options().apply {
            inScaled = false
        }

        rocket = BitmapFactory.decodeResource(context.resources, R.drawable.rocket, option)
        w = rocket.width / 2
        h = rocket.height / 2
        reset()
    }

    private fun reset() {
        x = w.toFloat()
        y = (sh - h).toFloat()
        ang = 0f
    }

    // 로켓 발사
    fun launch(px:Float, py:Float) {
        val rad = -Math.atan2((py-y-h).toDouble(), (px-x).toDouble()) // 각각 크기의 절반 만큼 빼줘야 정확한 위치에 그려짐.

        // 발싸 각도에 속도를 곱해서 수평/수직 속도를 구한다.
        dir.x += cos(rad).toFloat() * speed // 속도가 누적된다.
        dir.y += -sin(rad).toFloat() * speed
    }

    fun update():Boolean {
        var canRun = true

        // 중력
        // 로켓의 상승 속도를 순간 중력만큼 줄인다.
        dir.y += gravity * Time.deltaTime

        // 이동
        x += dir.x * Time.deltaTime
        y += dir.y * Time.deltaTime

        //로켓 회전
        val rad  = -Math.atan2(dir.y.toDouble(), dir.x.toDouble())
        // 수학의 회전방향(ccw)를 Canvas 의 회전방향으로 바꾼다. -> 시계 방향.
        // 최고 기준이 90도 이기 때문에 90도에서 역방향을 빼줘야 시계 방향의 각도가 구해진다.
        ang = 90 - Math.toDegrees(rad).toFloat()

        if(x > sw + h*2 || y > sh + h*2) {
            reset()
            canRun = false
        }
        return canRun
    }
}