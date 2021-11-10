package com.example.project

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_viewpager.*
import java.io.InputStream


class viewpager : AppCompatActivity() {
    lateinit var rs: Cursor
    private var imagesList = mutableListOf<Bitmap>()
    private var titleslist = mutableListOf<String>()
    private var listasecundaria = mutableListOf<String>()
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

        for (i in  listasecundaria){
            //rs.moveToPosition(0)
            //var path = rs?.getString(0)
            var imagen = BitmapFactory.decodeFile(i)
            addToList(listasecundaria[0], imagen)

        }
    }
    private fun listImages(){
        var cols = listOf<String>(MediaStore.Images.Thumbnails.DATA).toTypedArray()
        rs = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cols,null,null,null)!!

        if(rs!=null){
            while (rs!!.moveToNext()){
                listasecundaria.add(rs.getString(0))
                Toast.makeText(applicationContext,"Prueba",Toast.LENGTH_SHORT).show()
            }
        }else{

            Toast.makeText(applicationContext,"No funciona :(",Toast.LENGTH_SHORT).show()
        }

    }
}