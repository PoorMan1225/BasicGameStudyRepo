package com.rjhwork.mycompany.floatingbubble3t

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Bubble(private val context:Context,
             val sw:Int,
             val sh:Int) {

    var bubble: Bitmap                  // 비눗방울 비트맵.
    var x:Float = 0F; var y:Float = 0F  // 비눗방울의 위치
    var r:Int                           // 비눗 방울 반지름

    private var speed:Int = 0           // 비눗 방울 속도.
    private val dir = PointF()          // 이동 방향과 속도가 설정된 Vector 이다.
    private val rnd = Random()          // 비눗방울 크기 랜덤.

    var isDead:Boolean = false  // 삭제 표시를 위한 변수.

    init {
        // 비눗 방울 크기 랜덤하게 설정.
        r = rnd.nextInt(71) + 50 //50 ~ 120

        // 비눗방울 만들기.
        bubble = BitmapFactory.decodeResource(context.resources, R.drawable.bubble)
        bubble = Bitmap.createScaledBitmap(bubble, r * 2, r * 2, true)

        // 비눗 방울 초기설정
        initBubble()
    }

    private fun initBubble() {

        // 이동 속도
        speed = rnd.nextInt(51) + 150 // 초속 150~200 픽셀

        // 이동 방향
        val rad = Math.toRadians(rnd.nextInt(360).toDouble())

        // 랜덤한 값에 따라서 속도가 올라감.
        dir.x = cos(rad).toFloat() * speed
        dir.y = -sin(rad).toFloat() * speed

        // 초기 위치 = 화면 전체
        // 화면의 경계에 걸치지 않는 범위에서 랜덤하게 처리함.
        x = (rnd.nextInt(sw - r * 4) + r * 2).toFloat() // 아마 반지름을 고려해서 *4 를 해준듯 함.
        y = (rnd.nextInt(sh - r * 4) + r * 2).toFloat()

    }

    // 비눗 방울 이동.
    fun update() {

        x += dir.x * Time.deltaTime
        y += dir.y * Time.deltaTime

        if(x<r || x > sw - r) {
            dir.x = -dir.x
            x += dir.x * Time.deltaTime
        }

        if(y < r || y > sh - r) {
            dir.y = -dir.y
            y += dir.y * Time.deltaTime
        }
    }

    /**
     * mSmall 이 사용중이면 기다렸다가 자신의 차례가 오면 자료를
     * 추가하는 것이다.
     */
    fun hitTest(px:Float, py:Float): Boolean {

        val dist = (x - px) * (x - px) + (y - py) * (y - py)
        if(dist < r*r) {
            val cnt = rnd.nextInt(6) + 25           // 추가할 파편의 수 설정
            // GameView 의 mSmall 을 동기화.
            synchronized(GameView.mSmall) {
                (1..cnt).forEach { _ ->
                    GameView.mSmall.add(SmallBubble(context, sw, sh, px, py)) // 파편 추가.
                }
            }
            isDead = true
        }
        return isDead
    }
}