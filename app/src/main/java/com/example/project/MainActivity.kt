package com.example.project

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception
import android.graphics.BitmapFactory
import java.nio.ByteBuffer
import android.R.attr.bitmap
import android.speech.tts.TextToSpeech
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.lang.Math.abs
import java.util.*


class MainActivity : AppCompatActivity(),GestureDetector.OnGestureListener {

    lateinit var btngallery : Button
    lateinit var btnsettings: Button
    lateinit var btncamera : Button

    private val TAG  = "MyTag"


    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    lateinit var inputImage: InputImage


    lateinit var imagelabeler: ImageLabeler

    lateinit var result :String
    lateinit var image : InputImage

    lateinit var  gestureDetector: GestureDetector
    var x2:Float = 0.0f
    var x1:Float = 0.0f
    var y2:Float = 0.0f
    var y1:Float = 0.0f


    private var tts : TextToSpeech? = null
    companion object{
        const val MIN_DISTANCE = 150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnsettings = findViewById(R.id.btnSettings)
        btngallery = findViewById(R.id.btnGallery)
        btncamera = findViewById(R.id.btnCamera)

        imagelabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        gestureDetector = GestureDetector(this,this)

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        val photo=data?.extras?.get("data") as Bitmap
                        //ivPicture.setImageBitmap(photo)
                        inputImage = InputImage.fromBitmap(photo,0)
                        image = inputImage
                        processImage()
                    }catch (e: Exception){

                    }
                }
            }
        )
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        inputImage = InputImage.fromFilePath(this@MainActivity,data?.data)
                        //ivPicture.setImageURI(data?.data)
                        image = inputImage
                        Model.instance().setImage(data?.data)
                        processImage()

                    }catch (e: Exception){

                    }
                }
            }
        )



        btnsettings.setOnClickListener {
            val settings = Intent(this, SettingsActivity::class.java)
            startActivity(settings)
        }
        btngallery.setOnClickListener {
            val storageIntent = Intent()
            storageIntent.setType("image/*")
            storageIntent.setAction(Intent.ACTION_GET_CONTENT)
            galleryLauncher.launch(storageIntent)


        }
        btncamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)

        }
        if (Model.instance().getIndications() == true){
            speak()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        when(event?.action){
            0->
            {
                x1= event.x
                y1= event.y
            }
            1->
            {
                x2= event.x
                y2= event.y
                val valueX:Float = x2-x1
                val valueY:Float = y2-y1
                if(abs(valueX)> MIN_DISTANCE){
                    if(x2>x1){
                        //Toast.makeText(this,"RightSwipe",Toast.LENGTH_SHORT).show()
                        val storageIntent = Intent()
                        storageIntent.setType("image/*")
                        storageIntent.setAction(Intent.ACTION_GET_CONTENT)
                        galleryLauncher.launch(storageIntent)
                    }else{
                        //Toast.makeText(this,"Left Swipe",Toast.LENGTH_SHORT).show()
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(cameraIntent)
                    }
                }else if(abs(valueY)> MIN_DISTANCE){
                    if(y2>y1){
                        Toast.makeText(this,"BottomSwipe",Toast.LENGTH_SHORT).show()
                        val settings = Intent(this, SettingsActivity::class.java)
                        startActivity(settings)
                    }else{
                        Toast.makeText(this,"Top Swipe",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return super.onTouchEvent(event)
    }

    private fun processImage() {
        imagelabeler.process(inputImage)
            .addOnSuccessListener {
                result =""

                for(label in it) {
                    result = result + "\n"+ label.text
                }
                //tvResult.text = result
                nextthing()
            }.addOnFailureListener {
                Log.d(TAG,"processImage: ${it.message}")
            }
    }
    private fun nextthing(){
        val galleryIntent = Intent(this, imageActivity::class.java)
        galleryIntent.putExtra("result", result)
        startActivity(galleryIntent)
    }
    private fun speak() {
        tts = TextToSpeech(applicationContext,TextToSpeech.OnInitListener {
            if(it==TextToSpeech.SUCCESS){
                tts!!.language = Locale.US
                tts!!.setSpeechRate((0.75f))
                tts!!.speak("Swipe right to select image, swipe left to take a picture, swipe down to go to settings ",TextToSpeech.QUEUE_FLUSH,null)

            }
        })
    }




    override fun onDown(p0: MotionEvent?): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
        //TODO("Not yet implemented")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
        //TODO("Not yet implemented")
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        //TODO("Not yet implemented")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //TODO("Not yet implemented")
        return false
    }
}