package com.ahargunyllib.growth.model

data class QRScanData(
    val type: String = "",
    val weight: Int = 0,
    val timestamp: Long = 0L,
    val points: Int = 0,
    val locationId: String = ""
)
