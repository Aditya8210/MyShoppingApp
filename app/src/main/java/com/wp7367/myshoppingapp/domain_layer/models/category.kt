package com.wp7367.myshoppingapp.domain_layer.models

data class category(

    var name:String = "",
    val date: Long = System.currentTimeMillis(),
    var imageUri:String = "",
)