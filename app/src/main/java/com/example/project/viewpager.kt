package com.example.project

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import kotlinx.android.synthetic.main.activity_viewpager.*
import java.io.InputStream
import java.lang.Exception


class viewpager : AppCompatActivity() {
    lateinit var rs: Cursor
    private var imagesList = mutableListOf<Bitmap>()
    private var titleslist = mutableListOf<String>()
    private var listasecundaria = mutableListOf<String>()

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var inputImage: InputImage
    lateinit var image : InputImage
    lateinit var imagelabeler: ImageLabeler
    lateinit var result :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)

        if (ContextCompat.checkSelfPermission(this@viewpager,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this@viewpager,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),121)
        }else{
            listImages()
        }

        postToList()

        view_pager2.adapter = ViewPagerAdapter(titleslist,imagesList)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        inputImage = InputImage.fromFilePath(this@viewpager,data?.data)
                        //ivPicture.setImageURI(data?.data)
                        image = inputImage
                        Model.instance().setImage(data?.data)
                        processImage()

                    }catch (e: Exception){

                    }
                }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 121) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                listImages()
            }else{
                Toast.makeText(applicationContext,"Permission denied",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(applicationContext,"Ni eso",Toast.LENGTH_SHORT).show()
        }
    }
    private fun addToList(title: String, image:Bitmap){
        titleslist.add(title)
        imagesList.add(image)
    }
    private fun postToList(){

        for (i in  listasecundaria.indices){
            //rs.moveToPosition(0)
            //var path = rs?.getString(0)
            var imagen = BitmapFactory.decodeFile(listasecundaria[i])
            addToList(listasecundaria[i], imagen)

        }
    }
    private fun listImages(){
        var cols = listOf<String>(MediaStore.Images.Thumbnails.DATA).toTypedArray()
        rs = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cols,null,null,null)!!
        var numimages = 0
        if(rs!=null){
            while (rs!!.moveToNext()){
                listasecundaria.add(rs.getString(0))
                numimages++
                if (numimages>30){
                    break;
                }
                //Toast.makeText(applicationContext,"Prueba",Toast.LENGTH_SHORT).show()
            }
        }else{

            Toast.makeText(applicationContext,"No funciona :(",Toast.LENGTH_SHORT).show()
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
    public fun nextthing(){
        val galleryIntent = Intent(this, imageActivity::class.java)
        galleryIntent.putExtra("result", result)
        startActivity(galleryIntent)
    }
}