package com.ahargunyllib.growth.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Mission(
    val id: String = "",
    val icon: ImageVector,
    val category: String,
    val description: String,
    val points: Int,
    val progress: Float
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