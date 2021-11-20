package com.example.missilecommand_kotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF


class Bomb(center: PointF) : Figure {
    val maxSize = 80
    var radius: Float

    var center: PointF
    private val paint: Paint

    override fun update(dimensions: RectF?) {
        if (radius <= maxSize) radius += 1f
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawCircle(center.x, center.y, radius, paint)
    }

    init {
        this.center = PointF(center.x, center.y)
        radius = 1f
        paint = Paint()
        paint.setARGB(255, 255, 0, 0)
    }

}
