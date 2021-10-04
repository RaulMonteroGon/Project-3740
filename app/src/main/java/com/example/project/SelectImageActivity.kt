package com.example.project

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.IOException
import android.graphics.Bitmap
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import android.R.attr.data
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler


class SelectImageActivity : AppCompatActivity() {
    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableMultipleObjects()
        .enableClassification()  // Optional
        .build()
    val objectDetector = ObjectDetection.getClient(options)

    lateinit var imageView: ImageView
    lateinit var resultTv : TextView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var image: InputImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)

        val btnsettingsimg : Button = findViewById(R.id.btn_settingsimg)
        imageView = findViewById(R.id.imageView)
        resultTv = findViewById(R.id.textView)

        btnsettingsimg.setOnClickListener {
            /*val settings = Intent(this, SettingsActivity::class.java)
            startActivity(settings)*/
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val image = InputImage.fromBitmap(bitmap, 0)


            objectDetector.process(image)
                .addOnSuccessListener { detectedObjects ->
                    for (detectedObject in detectedObjects) {
                        val boundingBox = detectedObject.boundingBox
                        val trackingId = detectedObject.trackingId
                        val text = detectedObject.labels.firstOrNull()?.text?:"Undefined"
                        /*val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
                        toast.show()*/
                        resultTv.append(text + "      "+"\n" )

                    }
                }
                .addOnFailureListener { e ->

                }

        }
    }
}
