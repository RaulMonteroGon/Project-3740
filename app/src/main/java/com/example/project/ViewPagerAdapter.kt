package com.example.project

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private var title: List<String>,private var images:List<Bitmap>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {
    inner class Pager2ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.ivImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvPath)
        init {
            itemImage.setOnClickListener{v:View->

                Toast.makeText(itemView.context,"Has clickado en la imagen ${position+1}",Toast.LENGTH_SHORT).show()

            }
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