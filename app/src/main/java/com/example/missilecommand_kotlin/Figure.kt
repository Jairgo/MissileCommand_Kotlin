package com.example.missilecommand_kotlin

import android.graphics.Canvas
import android.graphics.RectF


interface Figure {
    fun update(dimensions: RectF?)
    fun draw(canvas: Canvas?)
}
