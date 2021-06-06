package com.rjhwork.mycompany.floatingbubble3t

// Time Class 는 매 프레임마다 한 번만 계산되므로 스레드나 핸들러에서
// 호출하면 된다.
class Time {
    companion object {
        var currentTime = System.nanoTime()
        var deltaTime = 0f

        fun update() {
            deltaTime = (System.nanoTime() - currentTime) / 1000000000f
            currentTime = System.nanoTime()
        }
    }
}