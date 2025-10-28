package com.ahargunyllib.growth.model

enum class CollectionStatus (
    val value: String,
    val label: String
){
    SUCCESS(value = "success", "Berhasil"),
    FAILED(value = "failed", "Gagal"),
    PENDING(value = "pending", "Pending")
}

data class Collection (
    val id: String = "",
    val userId: String = "",
    val partnerId: String = "",
    val totalWeightKg: Int = 0,
    val receivedPoints: Int = 0,
    val status: CollectionStatus = CollectionStatus.PENDING,
    val createdAt: String = ""
)