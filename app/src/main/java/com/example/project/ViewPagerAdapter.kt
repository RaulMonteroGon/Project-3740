package com.example.project

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.util.*

class ViewPagerAdapter(private var title: List<String>,private var images:List<Bitmap>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {
    inner class Pager2ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.ivImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvPath)

        private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
        lateinit var inputImage: InputImage
        lateinit var image : InputImage
        lateinit var imagelabeler: ImageLabeler
        lateinit var result :String

        private var tts : TextToSpeech? = null

        init {
            imagelabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            itemImage.setOnClickListener{v:View->

                val position = adapterPosition
                //Toast.makeText(itemView.context,title[position],Toast.LENGTH_SHORT).show()

                inputImage = InputImage.fromFilePath(itemView.context, Uri.fromFile(File(title[position])))
                //ivPicture.setImageURI(data?.data)
                //image = inputImage
                Model.instance().setImage(Uri.fromFile(File(title[position])))
                processImage()



            }
        }
        private fun processImage() {
            imagelabeler.process(inputImage)
                .addOnSuccessListener {
                    result =""

                    for(label in it) {
                        result = result + "\n"+ label.text
                    }
                    Toast.makeText(itemView.context,result,Toast.LENGTH_SHORT).show()
                    speak()
                }.addOnFailureListener {
                    Log.d(ContentValues.TAG,"processImage: ${it.message}")
                }
        }
        private fun speak() {
            tts = TextToSpeech(itemView.context,TextToSpeech.OnInitListener {
                if(it==TextToSpeech.SUCCESS){
                    tts!!.language = Locale.US
                    tts!!.setSpeechRate((0.5f))
                    tts!!.speak(result,TextToSpeech.QUEUE_FLUSH,null)

                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        //holder.itemImage.setImageResource(images[position])
        holder.itemImage.setImageBitmap(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }


}