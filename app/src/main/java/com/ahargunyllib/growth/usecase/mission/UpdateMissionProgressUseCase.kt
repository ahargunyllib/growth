package com.ahargunyllib.growth.usecase.mission

import android.util.Log
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.MissionProgress
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class UpdateMissionProgressUseCase @Inject constructor(
    private val missionRepository: MissionRepository
) {
    suspend operator fun invoke(
        missionId: String,
        incrementValue: Int
    ): Resource<Boolean> {
        try {
            // Get current progress
            val progressResult = missionRepository.getMissionProgress(missionId)

            when (progressResult) {
                is Resource.Success -> {
                    val existingProgress = progressResult.data

                    if (existingProgress != null) {
                        // Update existing progress
                        val updatedProgress = existingProgress.copy(
                            progressValue = existingProgress.progressValue + incrementValue
                        )
                        return missionRepository.updateMissionProgress(updatedProgress)
                    } else {
                        // No existing progress, fetch the mission to get targetValue
                        val missionResult = missionRepository.getMissionWithProgress(missionId)

                        when (missionResult) {
                            is Resource.Success -> {
                                val missionWithProgress = missionResult.data

                                if (missionWithProgress != null) {
                                    val newProgress = MissionProgress(
                                        missionId = missionId,
                                        progressValue = incrementValue,
                                        targetValue = missionWithProgress.mission.targetValue
                                    )
                                    return missionRepository.createMissionProgress(newProgress)
                                } else {
                                    return Resource.Error("Mission not found")
                                }
                            }
                            is Resource.Error -> {
                                return Resource.Error(missionResult.message ?: "Failed to fetch mission")
                            }
                            is Resource.Loading -> {
                                return Resource.Loading()
                            }
                        }
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
            Log.e("UpdateMissionProgressUseCase", "Error: ${e.message}")
            return Resource.Error(e.message ?: "Failed to update mission progress")
        }
    }
}
