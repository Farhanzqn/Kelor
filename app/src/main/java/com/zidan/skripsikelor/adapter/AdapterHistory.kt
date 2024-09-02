package com.zidan.skripsikelor.adapter

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.data.SuratItem
import com.zidan.skripsikelor.data.notification.NotificationHelper
import com.zidan.skripsikelor.data.notification.NotificationItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterHistory(private var items: List<SuratItem>, private val context: Context) :
    RecyclerView.Adapter<AdapterHistory.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
    private val sharedPreferences = context.getSharedPreferences("notified_surats", Context.MODE_PRIVATE)

    init {
        // Check status on init to show notification if needed
        checkAndNotify()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<SuratItem>) {
        items = newItems
        notifyDataSetChanged()
        checkAndNotify() // Re-check after updating data
    }

    private fun checkAndNotify() {
        if (items.isNotEmpty()) {
            val topItem = items[0]
            val uniqueKey = "${topItem.document_name}_${topItem.created_at}"

            val isNotified = sharedPreferences.getBoolean(uniqueKey, false)

            if (topItem.status == "Sudah Ditandatangani" && !isNotified) {
                // Ambil informasi NIK dari SharedPreferences
                val userNik = sharedPreferences.getString("user_nik", "") ?: ""

                // Buat notifikasi untuk NIK tersebut
                val notification = NotificationItem(
                    message = "Surat ${topItem.document_name} sudah selesai.",
                    timestamp = System.currentTimeMillis()
                )
                NotificationHelper.addNotification(userNik, notification)

                // Mark this surat as notified using the unique key
                sharedPreferences.edit().putBoolean(uniqueKey, true).apply()
            }
        }
    }

     fun postNotification(context: Context, message: String) {
        val channelId = "example_channel_id"
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Surat Selesai")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Handle permission if necessary
                return
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "example_channel_id"
            val channelName = "Surat Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifikasi untuk status surat"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val documentName: TextView = itemView.findViewById(R.id.namaSurat)
        private val status: TextView = itemView.findViewById(R.id.statusSurat)
        private val createdAt: TextView = itemView.findViewById(R.id.tanggalSurat)

        fun bind(item: SuratItem) {
            documentName.text = item.document_name
            status.text = item.status

            val colorRes = if (item.status == "Sudah Ditandatangani") {
                R.color.dodgerGreen // Green color for signed documents
            } else {
                R.color.red // Red color for unsigned documents
            }
            status.setTextColor(ContextCompat.getColor(itemView.context, colorRes))

            createdAt.text = formatDate(item.created_at, inputDateFormat, outputDateFormat)
        }

        private fun formatDate(dateString: String, inputFormat: SimpleDateFormat, outputFormat: SimpleDateFormat): String {
            return try {
                val parsedDate = inputFormat.parse(dateString)
                if (parsedDate != null) {
                    outputFormat.format(parsedDate)
                } else {
                    "Invalid Date"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                "Invalid Date"
            }
        }
    }
}
