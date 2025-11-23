package com.ahargunyllib.growth.model

enum class ExchangeMethodType(
    val value: String,
    val label: String,
    val category: String
) {
    DANA(value = "dana", label = "Dana", category = "E-Wallet"),
    BRI(value = "bri", label = "BRI", category = "Bank"),
    MANDIRI(value = "mandiri", label = "Mandiri", category = "Bank"),
    BNI(value = "bni", label = "BNI", category = "Bank"),
    BCA(value = "bca", label = "BCA", category = "Bank")
}

data class ExchangeMethod(
    val id: String = "",
    val type: ExchangeMethodType = ExchangeMethodType.DANA,
    val name: String = "",
    val description: String = "",
    val iconResId: Int = 0,
    val minAmount: Int = 100, // Minimum points to exchange
    val maxAmount: Int = 10000, // Maximum points per transaction
    val conversionRate: Float = 100.0f, // Points to IDR ratio (100 points = 10,000 IDR = 1 point = 100 IDR)
    val adminFee: Int = 0, // Admin fee in points
    val isActive: Boolean = true
)

enum class ExchangeStatus(
    val value: String,
    val label: String
) {
    SUCCESS(value = "success", label = "Berhasil"),
    FAILED(value = "failed", label = "Gagal"),
    PENDING(value = "pending", label = "Pending"),
    PROCESSING(value = "processing", label = "Diproses")
}

data class ExchangeTransaction(
    val id: String = "",
    val userId: String = "",
    val methodType: ExchangeMethodType = ExchangeMethodType.DANA,
    val accountNumber: String = "", // User's account number for the selected method
    val accountName: String = "", // User's account name
    val pointsExchanged: Int = 0, // Total points deducted
    val amountReceived: Int = 0, // Amount in IDR received by user
    val adminFee: Int = 0,
    val status: ExchangeStatus = ExchangeStatus.PENDING,
    val createdAt: String = "",
    val processedAt: String = "",
    val notes: String = ""
)
