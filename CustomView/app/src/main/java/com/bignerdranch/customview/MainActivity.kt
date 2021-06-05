package com.bignerdranch.customview

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private val gameView:GameView by lazy {
        findViewById(R.id.gameView)
    }

    private val toolbar:Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = "고양이"
        
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return false

        menu.apply {
            add(0,1,0, "Handler 시작")
            add(0,2,1, "Handler 중지")
            add(0,3,2, "프로그램 종료")
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> gameView.handler?.post(gameView.repeatAction)
            2 -> gameView.handler?.removeCallbacks(gameView.repeatAction)
            3 -> finish()
        }
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}