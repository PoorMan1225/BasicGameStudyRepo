package com.rjhwork.mycompany.rocket

class Time {
    companion object {
        private var currentTime = System.nanoTime()
        var deltaTime = 0f

        fun update() {
            deltaTime = (System.nanoTime() - currentTime) / 1000000000f
            currentTime = System.nanoTime()
        }
    }
}