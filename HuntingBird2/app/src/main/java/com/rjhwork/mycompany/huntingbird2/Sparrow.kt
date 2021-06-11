package com.rjhwork.mycompany.huntingbird2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import java.util.*

class Sparrow(private val sw:Int,       // 화면 넓이
              private val sh:Int) {     // 화면 높이

    private val rect = RectF()          // 터치 영역
    private var speed:Int = 0           // 이동 속도
    private val dir = PointF()          // 이동 방향

    private var animTime:Float = 0.0f   // 이미지를 그린 후 다음 이미지까지 지연할 시간
    private var animSpan:Float = 0.0f   // 이미지를 그린 후 현재 프레임 까지 경과한 시간
    private var animNum:Int = 0         // 현재 그린 이미지 번호.

    var x:Float = 0f                    // 참새 위치
    var y:Float = 0f                    // 참새 위치

    var w:Int = CommonResources.bw      // 참새 크기 의 절반.
    var h:Int = CommonResources.bh      // 참새 크기 의 절반.

    var bird:Bitmap = CommonResources.listBirds[0]  // 현재 이미지
    var ang:Int = 0                     // 추락할 때 참새 회전
    var isDead:Boolean = false          // 참새 사망.

    init {
        iniSparrow()
    }

    // Class 초기화
    private fun iniSparrow() {
        val rand = Random()

        // 이동 속도
        speed = rand.nextInt(101) + 700 // 700 ~ 800

        // 각 프레임의 지연시간 이동속도가 랜덤하기 때문에 지연시간도
        // 각각 다르게 줘야한다.
        // 빨리 이동하는 참새는 애니메이션 간격이 짧고, 느린 참새는
        // 길게 처리하기 위함. 애니메이션에 이동 속도를 반영하지 않으면
        // 날갯짓은 열심히 하는데 느리게 이동하는 참새가 나타날 수 있다.
        animTime = 0.85f - (speed / 1000f) // 0.15 ~ 0.05초

        // 이동 방향과 속도
        dir.x = speed.toFloat()
        dir.y = 0f

        // 참새의 초기 위치
        x = (-w * 2).toFloat()          // 절반의 두배만큼 옆으로 가야 완전히 사라짐.
        y = (rand.nextInt(sh - 500) + 100).toFloat() // 수직 위치 랜덤하게 설정.
    }

    // 참새 이동
    fun update() {

        // 참새는 화면 왼쪽에서 오른쪽으로 수평 이동,
        // 추락 중인 참새는 수직 이동한다.
        // 참새가 화면에서 벗어나면 소멸 표시를 한다.
        x += dir.x * Time.deltaTime
        y += dir.y * Time.deltaTime

        animationBird()

        // 화면을 벗어 났는가?
        if(x > sw + w || y > sh + h) {
            isDead = true
        }
    }

    // 매 프레임마다 deltaTime 을 더해서 애니메이션 지연 시간과 비교한다.
    // 추락 중인 참새는 애니메이션 하지 않는다.
    private fun animationBird() {
        // 애니메이션 후 경과시간
        animSpan += Time.deltaTime

        // 추락중이면 애니메이션 없음
        if(dir.y > 0 || animSpan < animTime) return

        animSpan = 0f
        animNum++

        // 애니메이션 작업. 
        if(animNum >= 5) {
            animNum = 0
        }
        bird = CommonResources.listBirds[animNum]
    }

    // 참새 터치 영역의 판정
    fun hitTest(px:Float, py:Float): Boolean {
        // 추락 중이면 득점 없음

        if(dir.y < 0)
            return false

//        rect.set(x - w, y - h, x + w, y + h)
//
//        if(rect.contains(px, py)) {
//            dir.y = speed.toFloat() // y 축이 증가하면서 내려감.
//            dir.x = 0f
//
//            ang = 180               // 추락시 180도  회전.
//        }
//        return (dir.x == 0f)

        // 어차피 draw 할때 반지름을 빼서 그려주기 때문에 여기서는 그냥
        // 해당 좌표에 맞게 계산 하면됨.
        val dist = (px - x) * (px - x) + (py - y) * (py - y)
        if(dist < h * h * 0.7) {
            dir.y = speed.toFloat()
            dir.x = 0f

            ang = 180
        }
        return (dir.x == 0f)
    }
}