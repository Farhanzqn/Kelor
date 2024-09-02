package com.zidan.skripsikelor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.data.DataBerita


class AdapterBerita(
    private val beritaList: List<DataBerita>,
    private val onItemClick: (DataBerita) -> Unit
) : RecyclerView.Adapter<AdapterBerita.BeritaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_berita, parent, false)
        return BeritaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeritaViewHolder, position: Int) {
        val berita = beritaList[position]
        holder.bind(berita)
    }

    override fun getItemCount(): Int = beritaList.size

    inner class BeritaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.titleTextViews)
        private val image: ImageView = itemView.findViewById(R.id.thumbnailImageView)

        fun bind(berita: DataBerita) {
            title.text = berita.title
            image.setImageResource(berita.imageResId)

            itemView.setOnClickListener {
                onItemClick(berita)
            }
        }
    }
}