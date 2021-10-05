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
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnsettings = findViewById(R.id.btnSettings)
        btngallery = findViewById(R.id.btnGallery)
        btncamera = findViewById(R.id.btnCamera)

        imagelabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)



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
            /*
            val buffer: ByteBuffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

            val galleryIntent = Intent(this, imageActivity::class.java)

            val bs = ByteArrayOutputStream()
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, bs)
            galleryIntent.putExtra("image", bs.toByteArray())
            galleryIntent.putExtra("result", result)

            startActivity(galleryIntent)*/
        }
        btncamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)

            /*
            val buffer: ByteBuffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

            val photoIntent = Intent(this, imageActivity::class.java)

            val bs = ByteArrayOutputStream()
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, bs)
            photoIntent.putExtra("image", bs.toByteArray())
            photoIntent.putExtra("result", result)

            startActivity(photoIntent)*/

        }

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
}