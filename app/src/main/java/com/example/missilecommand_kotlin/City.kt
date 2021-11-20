package com.example.missilecommand_kotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF


class City(dimensiones: RectF, x: Float) : Figure {

    private val size = 150f
    private val left: Float
    private val top: Float
    private val right: Float
    private val bottom: Float
    var positionCity: PointF
    private val paint: Paint
    var isAlive: Boolean

    override fun update(dimensions: RectF?) { }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(left, top, right, bottom, paint)
    }

    init {
        positionCity = PointF(x, dimensiones.bottom)
        paint = Paint()
        paint.setARGB(128, 0, 255, 0)
        left = positionCity.x - size / 2f
        top = positionCity.y - size / 2f
        right = positionCity.x + size / 2f
        bottom = positionCity.y + size / 2f
        isAlive = true
    }

}
