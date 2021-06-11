package com.rjhwork.mycompany.bounceball

import android.graphics.Bitmap
import android.graphics.PointF

class BallClass(val sw:Int,      // 화면의 넓이
                val sh:Int,      // 화면의 높이
                var px:Float,    // 공의 위치
                var py:Float     // 공의 위치
                ) {

    private val ground = sh * 0.8f      // 바닥 높이. (공이 닿이는 부분)
    private val speed = 300f            // 이동(300 만큼 이동)
    private val rotAng = 120            // 회전 속도(초속)
    private val gravity = 1500f         // 중력
    private val bounce = 0.8f           // 반발 계수
    private val dir = PointF()          // 이동 방향.
    var ang = 0f                // 현재 각도
    var isDead = false                  // 소멸
    val r = CommonResources.r               // 공의 반지름
    val ball: Bitmap = CommonResources.ball // 비트맵

    init {
        dir.x = speed                   // 공의 이동 방향
        dir.y = 0f                      // 공의 이동 방향.
    }

    fun update() {
        // 현재의 회전 각도
        ang += rotAng * Time.deltaTime

        // 중력
        /**
         * 중력 가속도는 1/2Gt^2 인데 1/2G 를 화면에 적당하도록(1500 픽셀 만큼 아래로 내려가게끔 설정) 했으므로
         * gravity*t^2 을 계산해야 한다. (1) 번에서 t를 한번 곱해주고. (2) 번에서 한번 더 곱해주므로
         * gravity*t^2 를 계산한 셈이 된다. (1) 은 매 프레임마다 수직 이동 속도를 중력의 deltaTime 만큼증가시키므로
         * 시간의 흐름에 따라 속도가 변한다. 즉, 가속도가 생기며 속도의 순간 변화량은 'gravity x deltaTime' 이다.
         */
        dir.y += gravity * Time.deltaTime  //...(1)

        // 이동
        px += dir.x * Time.deltaTime       //...(2)
        py += dir.y * Time.deltaTime

        if(py > ground) {
            py = ground

            // 바닥과 충돌 후, 수직 이동속도를 반대 방향으로 설정하면 반사가 이루어진다.
            // bounce 는 반사 계수로, 초깃값이 0.8f로 되어 있다. 이 값을 1로 바꾸면 100% 반사
            // 할 것이다.
            dir.y = -dir.y * bounce // 반사
        }

        isDead = (px > sw + r)
    }
}