package com.wp7367.myshoppingapp.domain_layer.models

data class ProductModel(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val category: String = "",
    val image: String = "aditya123",
    val date: Long = System.currentTimeMillis(),
    val availableUnits: Int = 0,
    val isAvailable: Boolean = true,
    val createdBy: String= "",
    var productId: String = "",
)


