package com.zidan.skripsikelor.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.data.notification.AdapterNotifikasi
import com.zidan.skripsikelor.data.notification.NotificationHelper

class NotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterNotifikasi
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        recyclerView = view.findViewById(R.id.rv_notification)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Ambil NIK pengguna yang login
        val userNik = sharedPreferences.getString("user_nik", "") ?: ""

        // Ambil notifikasi untuk NIK pengguna tersebut
        val allNotifications = NotificationHelper.getNotificationsForUser(userNik)
        val filteredNotifications = allNotifications.filter { notification ->
            val uniqueKey = "${userNik}_${notification.message}_${notification.timestamp}"
            !sharedPreferences.getBoolean(uniqueKey, false)
        }.toMutableList()

        // Tampilkan notifikasi yang belum ditampilkan
        adapter = AdapterNotifikasi(filteredNotifications)
        recyclerView.adapter = adapter

        // Tandai semua notifikasi yang telah ditampilkan
        for (notification in filteredNotifications) {
            val uniqueKey = "${userNik}_${notification.message}_${notification.timestamp}"
            sharedPreferences.edit().putBoolean(uniqueKey, true).apply()
        }

        return view
    }
}