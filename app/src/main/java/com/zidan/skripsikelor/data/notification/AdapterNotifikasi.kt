package com.zidan.skripsikelor.data.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zidan.skripsikelor.R

data class NotificationItem(
    val message: String,
    val timestamp: Long = System.currentTimeMillis() // default to current time
)
class AdapterNotifikasi(private val notificationList: MutableList<NotificationItem>) :
    RecyclerView.Adapter<AdapterNotifikasi.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)
    }

    override fun getItemCount() = notificationList.size

    // Menambahkan notifikasi baru ke dalam daftar
    fun addNotification(notification: NotificationItem) {
        notificationList.add(notification)
        notifyItemInserted(notificationList.size - 1)
    }

    // Menghapus notifikasi dari daftar
    fun removeNotification(position: Int) {
        if (position >= 0 && position < notificationList.size) {
            notificationList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.pesanNotif)

        fun bind(notification: NotificationItem) {
            messageTextView.text = notification.message
        }
    }
}