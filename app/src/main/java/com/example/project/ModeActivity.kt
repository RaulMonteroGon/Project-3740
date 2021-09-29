package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class ModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        val btnreselect : Button = findViewById(R.id.btn_reselect)
        val btnsettings : Button = findViewById(R.id.btn_settings)


        btnreselect.setOnClickListener {
            val selectimg = Intent(this, SelectImageActivity::class.java)
            startActivity(selectimg)
            finish()
        }
        btnsettings.setOnClickListener {
            val settings = Intent(this, SettingsActivity::class.java)
            startActivity(settings)
        }


    }
}
