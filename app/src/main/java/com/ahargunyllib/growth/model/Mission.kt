package com.ahargunyllib.growth.model

data class Mission(
    val id: String = "",
)

data class MissionProgress(
    val id: String = "",
    val missionId: String = "",
    val userId: String = "",
    val progressValue: Int = 0,
    val targetValue: Int = 0,
    val createdAt: String = ""
)

data class MissionCompletion(
    val id: String = "",
    val missionId: String = "",
    val userId: String = "",
    val rewardPoints: Int = 0,
    val isClaimed: Boolean = false,
    val createdAt: String = ""
)