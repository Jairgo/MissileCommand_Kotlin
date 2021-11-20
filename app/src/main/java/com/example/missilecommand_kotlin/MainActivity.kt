package com.example.missilecommand_kotlin

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.Toast


class MainActivity : Activity() {
    private var board: Board? = null
    private val msg: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // Elimina la barra superior de la app
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main);

        // Pone la orientación del celular en ladscape y FullScreen por default
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // Cambia de color el fondo de la actividad
        window.decorView.setBackgroundColor(Color.BLACK)
        newGame()
        setContentView(board)
    }

    private fun newGame() {
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        board = Board(this)
        board!!.setDimensions(0f, 0f, displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels.toFloat())
        board!!.dimensions?.let { board!!.addCities(it) }
    }

    override fun onResume() {
        super.onResume()
        board?.resume()
    }

    override fun onPause() {
        super.onPause()
        board?.pause()
    }
}