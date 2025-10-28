package com.ahargunyllib.growth.model

data class PointAccount(
    val ownerId: String = "",
    val balance: Int = 0,
)

enum class PointPostingRefType(
    val value: String,
    val label: String,
){
    DEPOSIT(value = "deposit", label = "Deposit"),
    MISSION_COMPLETION(value = "mission_completion", label = "Mission Completion"),
    WITHDRAWAL(value = "withdrawal", label = "Withdrawal")
}

data class PointPosting(
    val id: String = "",
    val accountId: String = "",
    val delta: Int = 0,
    val refType: PointPostingRefType = PointPostingRefType.DEPOSIT,
    val createdAt: String = "",
)