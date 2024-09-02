package com.zidan.skripsikelor.fragment.detailBerita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zidan.skripsikelor.R


class DetailBeritaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_berita, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val date = arguments?.getString("date")
        val imageResId = arguments?.getInt("imageResId", 0)
        val description = arguments?.getString("description")

        view.findViewById<ImageView>(R.id.iv_user).setImageResource(imageResId ?: 0)
        view.findViewById<TextView>(R.id.tv_judul).text = title
        view.findViewById<TextView>(R.id.tv_createAt).text = date
        view.findViewById<TextView>(R.id.tv_desc).text = description
    }
}
