package com.ahargunyllib.growth.model

data class Partner (
    val id: String = "",
    val name: String = "",
    val photoUrl: String? = null,
    val address: String = "",
    val latitude: Float = 0F,
    val longitude: Float = 0F
)