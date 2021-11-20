package com.example.missilecommand_kotlin

import android.graphics.*


class Battery(dimensiones: RectF, x: Float) : Figure {

    private val objective: PointF
    private val paint: Paint
    private val path: Path

    override fun update(dimensions: RectF?) { }

    override fun draw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    init {
        objective = PointF(x, dimensiones.bottom)
        val radius = 150f
        paint = Paint()
        paint.setARGB(255, 150, 152, 154)
        var radians = Math.toRadians(30.0).toFloat()
        val vertexA = PointF(
            (Math.cos(radians.toDouble()) * radius).toFloat() + objective.x,
            (Math.sin(radians.toDouble()) * radius).toFloat() + objective.y
        )
        radians = Math.toRadians(150.0).toFloat()
        val vertexB = PointF(
            (Math.cos(radians.toDouble()) * radius).toFloat() + objective.x,
            (Math.sin(radians.toDouble()) * radius).toFloat() + objective.y
        )
        radians = Math.toRadians(270.0).toFloat()
        val vertexC = PointF(
            (Math.cos(radians.toDouble()) * radius).toFloat() + objective.x,
            (Math.sin(radians.toDouble()) * radius).toFloat() + objective.y
        )
        path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(vertexA.x, vertexA.y)
        path.lineTo(vertexB.x, vertexB.y)
        path.lineTo(vertexC.x, vertexC.y)
        path.close()
    }

}
