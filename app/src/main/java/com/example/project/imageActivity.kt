package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import java.util.*



class imageActivity : AppCompatActivity(){

    lateinit var ivPicture : ImageView
    lateinit var tvResult : TextView
    private var tts : TextToSpeech? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


        ivPicture = findViewById(R.id.ivPhoto)
        tvResult = findViewById(R.id.tvResulttt)




        ivPicture.setImageURI(Model.instance().getImage() as Uri?)

        if(intent.hasExtra("result")){
            tvResult.text = intent.getStringExtra("result")
            //speak()
            }
        if(Model.instance().getIndications() == true) {
            speak()
        }
    }


    private fun speak() {
        tts = TextToSpeech(applicationContext,TextToSpeech.OnInitListener {
            if(it==TextToSpeech.SUCCESS){
                tts!!.language = Locale.US
                tts!!.setSpeechRate((0.5f))
                tts!!.speak(intent.getStringExtra("result"),TextToSpeech.QUEUE_FLUSH,null)

            }
        })
    }

}