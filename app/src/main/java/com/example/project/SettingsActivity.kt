package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val btnoff : Button = findViewById(R.id.btn_off)
        val btnon : Button = findViewById(R.id.btn_on)

        btnoff.setOnClickListener {
            Model.instance().setIndications(false)
        }
        btnon.setOnClickListener {
            Model.instance().setIndications(true)
        }
    }

}