package com.wp7367.myshoppingapp.domain_layer.models

data class orderModel(

    var orderId: String = "",
    var productId: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productQty: Any = "",
    val productFinalPrice: String = "",
    val productCategory: String = "",
    val productImageUrl: String = "",
    val color: String = "",
    val size: String = "",
    val date: Long = System.currentTimeMillis(),
    val transactionMethod: String = "",
    val transactionId: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val fullName: String = "",
    val address: String = "",
    var status: String = "Processing",

    )
