package com.example.missilecommand_kotlin

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class EndGameActivity : AppCompatActivity() {

    private lateinit var missiles: String
    private lateinit var btnMissiles: Button
    private lateinit var missilesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        setContentView(R.layout.activity_finish)
        intent = intent

        missilesText = findViewById<View>(R.id.missilesText) as TextView
        btnMissiles = findViewById<View>(R.id.btnNewGame) as Button

        if (intent != null) {
            missiles = intent!!.getStringExtra("missiles").toString()
            if (missiles != null) {
                missilesText!!.text = missiles
            }
        }

        btnMissiles!!.setOnClickListener {
            val endIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(endIntent)
        }
    }

}