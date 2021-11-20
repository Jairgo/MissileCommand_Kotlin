package com.example.missilecommand_kotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import java.util.*


class Missile(dimensiones: RectF, cities: ArrayList<City>) : Figure {

    private val origenX: Int
    private val origenY: Int
    private var rand = 0
    private val div: Int
    private val restax: Float
    private val restay: Float
    private val saltox: Float
    private val saltoy: Float
    var center: PointF
    var objective: PointF? = null
    private val paint: Paint
    private var random: Random? = null

    private fun setObjective(dimensiones: RectF, cities: ArrayList<City>) {
        val posibles = ArrayList<Int>()
        for (i in cities.indices) {
            if (cities[i].isAlive) {
                posibles.add(i)
            }
        }
        if (posibles.size > 0) {
            random = Random()
            rand = random!!.nextInt(posibles.size)
            rand = posibles[rand]
            when (rand) {
                0 -> objective = cities[0].positionCity
                1 -> objective = cities[1].positionCity
                2 -> objective = cities[2].positionCity
                3 -> objective = cities[3].positionCity
            }
        } else {
            objective = PointF(0F, dimensiones.bottom)
        }
        posibles.clear()
    }

    fun detectCollisionCity(): Int {
        return if (center.y >= objective!!.y) rand else -1
    }

    override fun update(dimensions: RectF?) {
        center.x = center.x - saltox
        center.y = center.y - saltoy
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawLine(origenX.toFloat(), origenY.toFloat(), center.x, center.y, paint)
    }

    init {
        val rand = Random()
        div = rand.nextInt(600 + 10) + 10
        origenY = dimensiones.top.toInt()
        origenX =
            (rand.nextInt((dimensiones.right + dimensiones.left).toInt()) + dimensiones.left).toInt()
        center = PointF(origenX.toFloat(), dimensiones.top)
        setObjective(dimensiones, cities)
        restax = center.x - objective!!.x
        restay = center.y - objective!!.y
        saltox = restax / div
        saltoy = restay / div
        paint = Paint()
        paint.setARGB(255, 255, 233, 0)
        paint.strokeWidth = 10f
    }
}
