package com.bignerdranch.randomnumber

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.isDigitsOnly
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private val fab: FloatingActionButton by lazy {
        findViewById(R.id.floating_action_button)
    }

    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    private val editText:EditText by lazy {
        findViewById(R.id.editText)
    }

    private val txtCount:TextView by lazy {
        findViewById(R.id.textCount)
    }

    private val txtResult:TextView by lazy {
        findViewById(R.id.textResult)
    }

    private val button:Button by lazy {
        findViewById(R.id.button)
    }

    private val rand = Random()
    private var count = 0
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBar()
        initFab()

        num = rand.nextInt(1001 - 500)
    }

    @SuppressLint("SetTextI18n")
    private fun clearField() {
        txtCount.text = "입력횟수 : $count"
        txtResult.text = ""
        editText.setText("")
    }

    private fun initBar() {
        title = "숫자 맞추기"
        setSupportActionBar(toolbar)
        button.setOnClickListener(listener)
    }

    private fun initFab() {
        fab.backgroundTintList = ColorStateList.valueOf(getColor(R.color.purple_200))
        fab.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_replay_24, null))
        fab.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener {
        when(it.id) {
            R.id.floating_action_button -> {
                num = rand.nextInt(1001 - 500)
                count = 0
                clearField()
            }
            R.id.button -> checkValue()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkValue() {
        var str = editText.text.toString()

        if(str.isBlank() || !str.isDigitsOnly()) {
            txtResult.text = "500 ~ 1000 사이의 숫자를 입력하세요."
            return
        }

        val n = str.toInt()
        str = when {
            n == num -> "정답입니다."
            n > num -> "보다는 작습니다."
            else -> "보다는 큽니다."
        }
        count++

        txtCount.text = "입력횟수 : $count"
        txtResult.text = str

        if(n != num) {
            editText.selectAll()
            editText.requestFocus()
        }
    }
}