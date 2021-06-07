package com.rjhwork.mycompany.huntingbird

import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class GameView(context: Context,
               attrs:AttributeSet? = null
): View(context, attrs) {

    private lateinit var mThread:GameThread
    private var imgBack:Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.back)
    private var w:Int = 0
    private var h:Int = 0

    // 배경 음악, 효과음, 사운드 id
    private val mPlayer:MediaPlayer = MediaPlayer.create(context, R.raw.rondo).apply {
        isLooping = true
        start()
    }

    private val attributes:AudioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    private val mSound:SoundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(5)
        .build()

    private val soundId:Int = mSound.load(context, R.raw.fire, 1)

    // 점수 표시용
    private var hit:Int = 0
    private var miss:Int = 0

    // 참새 생성 시간과 Paint
    private var makeTimer = 0f
    private val paint = Paint().apply {
        textSize = 20f
        color = Color.WHITE
    }

    private val mSparrow = Collections.synchronizedList(mutableListOf<Sparrow>())

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.w = w
        this.h = h

        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true)

        mThread = GameThread()
        mThread.start()
    }

    override fun onDetachedFromWindow() {
        mThread.canRun = false
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(imgBack, 0f, 0f, null)

        synchronized(mSparrow) {
            mSparrow.forEach {
                canvas.rotate(it.ang.toFloat(), it.x, it.y)
                canvas.drawBitmap(it.bird, it.x - it.w, it.y - it.h, null)
                canvas.rotate(-it.ang.toFloat(), it.x, it.y)
            }
        }

        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Hit : $hit", 100f, 100f, paint)

        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Miss : $miss", w - 100f, 100f, paint)
    }

    private fun makeSparrow() {
        // fps 에 따라서 정확한 0.5초마다
        // Sparrow 를 추가하기 위해서 델타 타임을 빼준다.
        makeTimer -= Time.deltaTime

        if(makeTimer <= 0) {
            makeTimer = 0.5f
            synchronized(mSparrow) {
                mSparrow.add(Sparrow(context, w, h))
            }
        }
    }

    private fun moveSparrow() {
        synchronized(mSparrow) {
            mSparrow.forEach {
                it.update()
            }
        }
    }

    private fun removeDead() {
        synchronized(mSparrow) {
            for(i in mSparrow.size-1 downTo 0) {
                if(mSparrow[i].isDead)
                    mSparrow.removeAt(i)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        if(event.action == MotionEvent.ACTION_DOWN) {
            fireBullet(event)
        }

        return true
    }

    private fun fireBullet(event: MotionEvent) {
        var isHit = false
        mSound.play(soundId, 1f, 1f, 1, 0, 1f)

        mSparrow.forEach {
            if (it.hitTest(event.x, event.y)) {
                isHit = true
                return@forEach
            }
        }

        hit = if (isHit) hit + 1 else hit
        miss = if (isHit) miss else miss + 1
    }

    inner class GameThread: Thread() {
        var canRun:Boolean = true

        override fun run() {
            while (canRun) {
                try{
                    Time.update()
                    makeSparrow()
                    moveSparrow()
                    removeDead()
                    postInvalidate()
                    sleep(10)
                }catch (e:Exception) {

                }
            }
        }
    }
}