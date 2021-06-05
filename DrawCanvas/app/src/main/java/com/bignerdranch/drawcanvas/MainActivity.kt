package com.bignerdranch.drawcanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CanvasView(this))

        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }
}