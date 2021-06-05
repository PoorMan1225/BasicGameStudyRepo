package com.bignerdranch.rotatebitmap

import android.content.Context
import android.graphics.*
import android.view.View

class GameView(context: Context): View(context) {

    private var cx:Int = 0      // 화면의 중심
    private var cy:Int = 0      // 화면의 중심

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    private val n = 0           // 보여줘야 하는 이미지 인덱스
    private val mrg = 30f       // 여백.

    private val imgRes: List<Int> = listOf(     // imgRes
        R.drawable.top,
        R.drawable.right,
        R.drawable.down,
        R.drawable.left
    )

    private val rose:List<Bitmap> = listOf(     // decode image
        BitmapFactory.decodeResource(resources, imgRes[0]),
        BitmapFactory.decodeResource(resources, imgRes[1]),
        BitmapFactory.decodeResource(resources, imgRes[2]),
        BitmapFactory.decodeResource(resources, imgRes[3])
    )

    private val rw:List<Int> = listOf(
        rose[0].width / 2,
        0,
        rose[2].width / 2,
        rose[3].width
    )

    private val rh:List<Int> = listOf(
        rose[0].height,
        rose[1].height/2,
        0,
        rose[3].height/2
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.cy = w / 2
        this.cx = h / 2
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawBitmap(rose[n], mrg, mrg, null)  // bitmap 이라서 paint 객체 설정할 필요 없음.

        canvas.drawCircle(rw[n]+mrg, rh[n]+mrg, 20f, paint)

        val cnt = 16
        val ang = 360f/cnt

        canvas.scale(0.9f, 0.9f, cx.toFloat(), cy.toFloat())

        for(i in 1..cnt) {
            // 해당 좌표에서 비트맵 을 그리기 때문에
            // 그리고 중간값 만큼 빼줘야 완전히 중간이기 때문에.
            canvas.drawBitmap(rose[n], (cx-rw[n]).toFloat(), (cy-rh[n]).toFloat(), null)
            canvas.rotate(ang, cx.toFloat(), cy.toFloat())
        }
    }
}