package com.bignerdranch.fortune

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.bignerdranch.fortune.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    val rand = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initFab()
        initListener()
    }

    private fun initListener() {
        binding.fab.setOnClickListener(listener)
        (0 until 4).forEach { i ->
            findViewById<Button>(R.id.button1 + i).setOnClickListener(listener)
        }
    }

    private fun initFab() {
        binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#cc0099"))
        binding.fab.setImageResource(R.drawable.ic_baseline_replay_24)
    }

    private val listener = View.OnClickListener { v ->
        when(v.id) {
            R.id.fab-> finish()
            else -> {
                val tag = findViewById<Button>(v.id).tag.toString()
                checkValue(tag)
            }
        }
    }

    private fun checkValue(tag: String) {
        val tag = tag.toInt()

        val msg = if (tag == rand.nextInt(4-1)) {
            "$tag 번 버튼 : 축하합니다! 당첨되셨습니다."
        }else {
            "$tag 번 버튼 : 안타깝습니다. 다음 기회에 도전하세요."
        }
        binding.textResult.text = msg
    }
}