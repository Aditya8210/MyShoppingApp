package com.wp7367.myshoppingapp.domain_layer.models

data class NotificationModel(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "order" // e.g., order, promo, account
)
