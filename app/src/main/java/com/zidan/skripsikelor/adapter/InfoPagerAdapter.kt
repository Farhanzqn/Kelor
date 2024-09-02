package com.zidan.skripsikelor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zidan.skripsikelor.data.DataInfo
import com.zidan.skripsikelor.R

class InfoPagerAdapter(private val placeList: List<DataInfo>) : RecyclerView.Adapter<InfoPagerAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.tvName.text = place.name
        holder.tvJumlah.text = place.jumlah
        holder.imageView.setImageResource(place.imageResId)
    }

    override fun getItemCount(): Int = placeList.size

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_jumlah)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}