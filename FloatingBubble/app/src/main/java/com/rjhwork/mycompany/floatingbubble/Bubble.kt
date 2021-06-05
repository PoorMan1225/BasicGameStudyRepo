package com.rjhwork.mycompany.floatingbubble

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

// Bubble 클래스는 View 나 Activity 를 상속 받지 않으므로
// Context 객체를 생성자로 받아야 한다.
// 화면의 크기, s,w, 비눗 방을의 초기위치 px, py
class Bubble(context:Context, val sw:Int, val sh:Int, px:Int, py:Int) {

    var bubble: Bitmap   // 비눗방울 비트맵.
    var x:Int; var y:Int // 비눗방울의 위치
    var bw:Int           // 비눗방울 크기.

    private var sx:Int // 비눗 방울의 이동 속도.
    private var sy:Int // 비눗 방울의 이동 속도.

    init {
        x = px
        y = py

        val rnd = Random()
        bw = rnd.nextInt(101) + 50
        bubble = BitmapFactory.decodeResource(context.resources, R.drawable.bubble)
        bubble = Bitmap.createScaledBitmap(bubble, bw * 2, bw * 2, true)

        // 비눗 방울 이동 속도.
        (rnd.nextInt(5) + 1).apply {
            sx = this
            sy = this
        }

        // 이동 방향을 +/- 로 설정.
        (rnd.nextInt(2)).apply {
            if(this != 0) {
                sx = -sx
                sy = -sy
            }
        }
    }

    // 비눗 방울이 이동하다 화면 좌우 경계에 닿으면
    // 반대 방향으로 반사하는 함수를 만든다.
    fun update() {
        x += sx
        y += sy

        // 좌우의 끝인가?
        if(x <= bw || x > sw - bw) {
            sx = -sx
            x += sx
        }

        // 상하의 끝인가?
        if(y < bw || y > sh - bw) {
            sy = -sy
            y += sy
        }
    }
}