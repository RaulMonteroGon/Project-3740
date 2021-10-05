package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.BitmapFactory

import android.graphics.Bitmap




class imageActivity : AppCompatActivity() {

    lateinit var ivPicture : ImageView
    lateinit var tvResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)



        ivPicture = findViewById(R.id.ivPhoto)
        tvResult = findViewById(R.id.tvResulttt)

        if (intent.hasExtra("byteArray")) {
            val bitmap = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("byteArray"),
                0,
                intent.getByteArrayExtra("byteArray")!!.size
            )
            ivPicture.setImageBitmap(bitmap)
        }
        if(intent.hasExtra("result")){
            tvResult.text = intent.getStringExtra("result")
            }
    }
}