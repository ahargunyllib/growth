package com.ahargunyllib.growth.usecase.mission

import android.util.Log
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.MissionCompletion
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class CompleteMissionUseCase @Inject constructor(
    private val missionRepository: MissionRepository
) {
    suspend operator fun invoke(missionId: String): Resource<MissionCompletion> {
        try {
            // Check if already completed
            val existingCompletionResult = missionRepository.getMissionCompletion(missionId)

            if (existingCompletionResult is Resource.Success && existingCompletionResult.data != null) {
                return Resource.Error("Mission already completed")
            }

            // Get mission progress
            val progressResult = missionRepository.getMissionProgress(missionId)

            when (progressResult) {
                is Resource.Success -> {
                    val progress = progressResult.data
                        ?: return Resource.Error("No progress found for this mission")

                    // Check if progress meets target
                    if (progress.progressValue < progress.targetValue) {
                        return Resource.Error("Mission not yet completed. Progress: ${progress.progressValue}/${progress.targetValue}")
                    }

                    // Get mission to get reward points
                    val missionsResult = missionRepository.getAllMyMission()

                    if (missionsResult is Resource.Success) {
                        val mission = missionsResult.data?.find { it.id == missionId }
                            ?: return Resource.Error("Mission not found")

                        // Create completion record
                        val completion = MissionCompletion(
                            missionId = missionId,
                            rewardPoints = mission.pointReward,
                            claimed = false
                        )

                        val createResult = missionRepository.createMissionCompletion(completion)

                        return when (createResult) {
                            is Resource.Success -> Resource.Success(completion)
                            is Resource.Error -> Resource.Error(createResult.message ?: "Failed to complete mission")
                            is Resource.Loading -> Resource.Loading()
                        }
                    } else {
                        return Resource.Error("Failed to fetch mission details")
                    }
                }
                is Resource.Error -> {
                    return Resource.Error(progressResult.message ?: "Failed to get progress")
                }
                is Resource.Loading -> {
                    return Resource.Loading()
                }
            }
        } catch (e: Exception) {
            Log.e("CompleteMissionUseCase", "Error: ${e.message}")
            return Resource.Error(e.message ?: "Failed to complete mission")
        }
    }
}
