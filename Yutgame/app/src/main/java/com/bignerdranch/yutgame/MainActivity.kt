package com.bignerdranch.yutgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private val yutName = listOf("모", "도", "개", "걸", "윷")
    private val yutImg = listOf(R.drawable.yut_00,R.drawable.yut_0, R.drawable.yut_1)

    private val textResult:TextView by lazy {
        findViewById(R.id.textResult)
    }
    private val imageViews:List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.imageView01))
            add(findViewById(R.id.imageView02))
            add(findViewById(R.id.imageView03))
            add(findViewById(R.id.imageView04))
        }
    }

    private val rand = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(listener)
        findViewById<Button>(R.id.button).setOnClickListener(listener)
    }

    private val listener = View.OnClickListener { v ->
        if(v.id == R.id.button) {
            setGameResult()
        }
    }

    private fun setGameResult() {
        val check: Boolean
        var s = 0

        val p = rand.nextInt(2)
        if(p == 0) {
            s += -1
            check = true
        }else {
            check = false
        }
        imageViews[0].setImageResource(yutImg[p])

        (1 until 4).forEach { i ->
            val n = rand.nextInt(2)+1
            if(n == 2) {
               s += 1
            }
            imageViews[i].setImageResource(yutImg[n])
        }
        if(check && s == -1) textResult.text = "빽도"
        else if(check && s != -1) {
            s += 2
            textResult.text = yutName[s]
        }
        else if(check.not()) textResult.text = yutName[s]
    }
}