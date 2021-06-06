package com.rjhwork.mycompany.floatingbubble3

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
        // 그냥 s += sx 이런식으로 속도를 정하면 fps 가 빠른 기기에서는
        // 속도대로 움직이는 것이 아니라 빨리 움직이고 fps가 낮은 기기에서는
        // 천천히 움직일 것이다. 그렇기 때문에 deltaTime 을 구해서 프레임에 맞게
        // 같은 거리를 이동하도록 속도를 올려주거나 해야한다. deltaTime 은 값이 클 수록
        // 더 낮은 fps 를 사용하고 있다는 의미가 된다.

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

    // 위에서 x 좌표가 생성 될 수 있는 곳에서 반지름을 처리 해 줬으므로
    // 초기 위치 = 화면 전체
    // 화면의 경계에 걸치지 않는 범위에서 랜덤하게 처리함.
    // x = (rnd.nextInt(sw - r * 4) + r * 2).toFloat() // 아마 반지름을 고려해서 *4 를 해준듯 함.
    // y = (rnd.nextInt(sh - r * 4) + r * 2).toFloat()
    // 그래서 여기서는 해당 좌표에서 길이값으로 원내부인지 판단하면 된다.

    // onTouchEvent 에서 메소드에서 삭제할때 인터럽트가 발생할 수 있으므로
    // 삭제할 풍선들을 표시만 해주는 식으로 코드를 변경한다.
    fun hitTest(px:Float, py:Float): Boolean {

        val dist = (x - px) * (x - px) + (y - py) * (y - py)
        if(dist < r*r) {
            val cnt = rnd.nextInt(6) + 25           // 추가할 파편의 수 설정
            (1..cnt).forEach { _ ->
                GameView.mSmall.add(SmallBubble(context, sw, sh, px, py)) // 파편 추가.
            }
            isDead = true
        }
        return isDead
    }
}