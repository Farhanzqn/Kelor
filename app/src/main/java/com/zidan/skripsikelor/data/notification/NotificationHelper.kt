package com.zidan.skripsikelor.data.notification




object NotificationHelper {
    private val notificationsMap = mutableMapOf<String, MutableList<NotificationItem>>()

    fun addNotification(nik: String, notification: NotificationItem) {
        val userNotifications = notificationsMap[nik] ?: mutableListOf()
        userNotifications.add(notification)
        notificationsMap[nik] = userNotifications
    }

    fun getNotificationsForUser(nik: String): List<NotificationItem> {
        return notificationsMap[nik] ?: emptyList()
    }
}