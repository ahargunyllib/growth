package com.ahargunyllib.growth.model

import com.google.android.gms.maps.model.LatLng

data class TPS(
    val name: String,
    val address: String,
    val distance: String,
    val location: LatLng
)
