package com.rjhwork.mycompany.floatingbubble3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class SmallBubble(
    val context: Context,   // context
    val sw: Int,            // 화면 넓이
    val sh: Int,            // 화면 높이
    var px: Float,          // 현재 위치
    var py: Float           // 현재 위치
) {
    private val rand = Random()
    private val dir = PointF()

    // 이동속도를 랜덤하게 설정하면 폭파시의 파편처럼 랜덤하게 흩어지며
    // 값을 고정하면 둥근 모양으로 흩어지게 된다.
    private val speed: Int = rand.nextInt(201) + 300           // 속도 300 ~ 500
    private var life: Float = (rand.nextInt(6) + 10) / 10f     // 수명 1.0 ~ 1.5 초
    val r:Int = rand.nextInt(11) + 10                          // 반지름
    private val n:Int = rand.nextInt(6)                        // 이미지 번호.
    var alpha = 255                                                   // 투명도
    var isDead:Boolean = false                                        // 죽었는지 표시.

    var bubble = BitmapFactory.decodeResource(context.resources, R.drawable.bubble) // 이미지 리소스

    private val rad = Math.toRadians(rand.nextInt(360).toDouble()) // 이동 방향

    init {
        dir.x = cos(rad).toFloat() * speed
        dir.y = -sin(rad).toFloat() * speed

        bubble = Bitmap.createScaledBitmap(bubble, r * 2, r * 2, true)
    }

    fun update() {

        px += dir.x * Time.deltaTime
        py += dir.y * Time.deltaTime

        // 남은 수명을 줄인다. deltaTime 은 직전 프레임과의 시간 차이 이므로
        life -= Time.deltaTime
        if(life < 0) {
            // 수명이 다하면 alpha 를 낮춰서 점점 투명하게 만든다.
            alpha -= 5
            if(alpha < 0) alpha = 0
        }

        // 수명이 다하거나 화면을 벗어나면 삭제 표시를 한다.
        if(alpha == 0 || px < -r || px > sw + r || py < -r || py > sh + r)  {
            isDead = true
        }
    }
}