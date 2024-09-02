package com.zidan.skripsikelor.fragment.history

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.SuratViewModelFactory
import com.zidan.skripsikelor.adapter.AdapterHistory
import com.zidan.skripsikelor.data.SuratItem
import com.zidan.skripsikelor.data.notification.NotificationHelper
import com.zidan.skripsikelor.data.notification.NotificationItem
import com.zidan.skripsikelor.data.repository.HistoryRepository
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.retrofit.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterHistory: AdapterHistory
    private lateinit var viewModel: HistoryViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.rv_history)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val listOfSuratItems = listOf<SuratItem>() // This should be your actual data

        adapterHistory = AdapterHistory(items = listOfSuratItems, context = requireContext())
        recyclerView.adapter = adapterHistory

        val historyRepository = HistoryRepository(RetrofitClient.apiService)
        val suratRepository = SuratRepository()
        val residentRepository = ResidentRepository(RetrofitClient.apiService, requireContext())
        val factory = SuratViewModelFactory(
            suratRepository,
            historyRepository,
            residentRepository
        )
        viewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)

        val nik = sharedPreferences.getString("user_nik", "") ?: ""
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        observeData(nik, token)

        swipeRefreshLayout.setOnRefreshListener {
            refreshData(nik, token)
        }

        return view
    }

    private fun observeData(nik: String, token: String) {
        viewModel.getDocumentHistory(nik, token).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if needed
                }

                is Result.Success -> {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

                    // Sort the history by date, with the most recent first
                    val sortedHistory = result.data.sortedByDescending { surat ->
                        dateFormat.parse(surat.created_at)?.time ?: 0L
                    }

                    adapterHistory.updateData(sortedHistory)

                    // Check if the top document is completed and create a notification
                    val topSurat = sortedHistory.firstOrNull()
                    if (topSurat?.status == "Sudah Ditandatangani") {
                        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val userNik = sharedPreferences.getString("user_nik", "") ?: ""
                        val uniqueKey = "${userNik}_${topSurat.document_name}_${topSurat.created_at}"

                        // Cek apakah notifikasi sudah pernah ditampilkan
                        val isNotified = sharedPreferences.getBoolean(uniqueKey, false)

                        if (!isNotified) {
                            // Buat pesan notifikasi
                            val message = "Surat kamu yang berjudul '${topSurat.document_name}' sudah selesai."

                            // Tambahkan notifikasi ke dalam NotificationHelper
                            val notification = NotificationItem(
                                message = message,
                                timestamp = System.currentTimeMillis()
                            )
                            NotificationHelper.addNotification(userNik, notification)

                            // Tampilkan notifikasi ke pengguna
                            adapterHistory.postNotification(requireContext(), message)

                            // Tandai bahwa notifikasi untuk surat ini sudah ditampilkan
                            sharedPreferences.edit().putBoolean(uniqueKey, true).apply()
                        }
                    }

                    swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
                }

                is Result.Error -> {
                    Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
                }
            }
        })
    }

    private fun refreshData(nik: String, token: String) {
        observeData(nik, token)
    }
}