package com.wp7367.myshoppingapp.domain_layer.models

data class shippingModel(
    var addressId: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val fullName: String = "",
    val address1: String = "",
    val address2: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    var selected: Boolean = false
)
