package com.ahargunyllib.growth.model

data class Mission(
    val id: String = "",
    val category: String = "",
    val description: String = "",
    val pointReward: Int = 0,
    val targetValue: Int = 0,
)

data class MissionProgress(
    val id: String = "",
    val missionId: String = "",
    val userId: String = "",
    val progressValue: Int = 0,
    val targetValue: Int = 0,
)

data class MissionCompletion(
    val id: String = "",
    val missionId: String = "",
    val userId: String = "",
    val rewardPoints: Int = 0,
    val claimed: Boolean = false,
    val createdAt: String = ""
)

// Combined data for UI display
data class MissionWithProgress(
    val mission: Mission,
    val progress: MissionProgress?,
    val completion: MissionCompletion?
) {
    val progressPercentage: Float
        get() = if (progress != null && mission.targetValue > 0) {
            (progress.progressValue.toFloat() / mission.targetValue.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

    val isCompleted: Boolean
        get() = completion != null

    val isClaimed: Boolean
        get() = completion?.claimed == true
}