package com.example.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.FruitAdapter.ViewHolder

class FruitAdapter(val dataList: ArrayList<Fruit>): RecyclerView.Adapter<FruitAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val fruitImage=itemView.findViewById<ImageView>(R.id.iv_fruitImg)
        val fruitName=itemView.findViewById<TextView>(R.id.tv_fruitName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fruititme, null, false)
        val viewHolder=ViewHolder(view)
        viewHolder.fruitImage.setOnClickListener(){
            val position=viewHolder.adapterPosition
            val fruitName = dataList.get(position).name
            Toast.makeText(view.context,"$fruitName",Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = dataList.get(position)
        holder.fruitName.setText(fruit.name)
        holder.fruitImage.setImageResource(fruit.image)

    }
}