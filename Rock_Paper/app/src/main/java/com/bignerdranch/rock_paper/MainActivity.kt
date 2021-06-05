package com.bignerdranch.rock_paper

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private val imgRes = listOf(R.drawable.image1, R.drawable.image3, R.drawable.image2)

    private var win = 0
    private var lose = 0

    private var random = Random()

    private val imageViewYou: ImageView by lazy {
        findViewById(R.id.imageView01)
    }

    private val imageViewCom: ImageView by lazy {
        findViewById(R.id.imageView02)
    }

    private val txtYou: TextView by lazy {
        findViewById(R.id.textYou)
    }

    private val txtCom: TextView by lazy {
        findViewById(R.id.textCom)
    }

    private val txtResult:TextView by lazy {
        findViewById(R.id.textResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        initListener()
        initGame()
    }

    private fun initListener() {
        (0..2).forEach { i ->
            findViewById<ImageButton>(R.id.imageButton01 + i).setOnClickListener(listener)
        }
    }

    private fun initGame() {
        win = 0
        lose = 0

        txtYou.text = "당신 : 0"
        txtCom.text = "단말기 : 0"
        txtResult.text = ""

        imageViewYou.setImageResource(R.drawable.image_you)
        imageViewCom.setImageResource(R.drawable.image_you)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, "다시 시작")
        menu.add(0, 2, 1, "종료")
        menu.add(0, 3, 2, "About")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> initGame()
            2 -> finishAffinity()
            3 -> {
                val v = findViewById<View>(R.id.imageButton01)
                Snackbar.make(v, "가위바위보 Ver 1.0", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show()
            }
        }
        return true
    }

    private val listener = object:View.OnClickListener {
        override fun onClick(v: View) {
            val tag = v.tag.toString()
            setGameResult(tag.toInt())
        }
    }

    private fun setGameResult(tag: Int) {
        val com = random.nextInt(3)
        setImages(tag, com)
        val result = tag - com

        when(result) {
            -2, 1 -> ++win
            -1, 2 -> ++lose
            else -> 0
        }
        updateUI(result)
    }

    private fun setImages(tag: Int, com: Int) {
        imageViewYou.setImageResource(imgRes[tag])

        // Bitmap 만들기
        val orgImg = BitmapFactory.decodeResource(resources, imgRes[com])

        // 이미지를 뒤집을 Matrix
        val matrix = Matrix()
        matrix.setScale(-1f, 1f)
        val revImg = Bitmap.createBitmap(orgImg, 0, 0, orgImg.width, orgImg.height, matrix, false)
        imageViewCom.setImageBitmap(revImg)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(result: Int) {
        txtCom.text = "단말기: $lose"
        txtYou.text = "당신: $win"

        txtResult.text = when(result) {
            -2, 1 -> "이겼습니다!"
            -1, 2 -> "졌습니다!"
            else -> "무승부입니다!"
        }
    }
}