package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class SettingsActivity : AppCompatActivity() {
    var sound : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        Model.instance().setIndications(false)
    }

}