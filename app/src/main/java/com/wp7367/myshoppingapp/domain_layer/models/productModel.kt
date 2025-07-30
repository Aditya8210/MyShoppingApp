package com.wp7367.myshoppingapp.domain_layer.models

data class productsModel(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val category: String = "",
    val image: String = "",
    val date: Long = System.currentTimeMillis(),
    val availableUnits: Int = 0,
    val isAvailable: Boolean = true,
    val productId: String = "",
    val createdBy: String,

    )