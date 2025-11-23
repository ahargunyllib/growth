package com.ahargunyllib.growth.usecase.mission

import android.util.Log
import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.usecase.point.CreatePointPostingUseCase
import com.ahargunyllib.growth.usecase.point.GetMyPointAccountUseCase
import com.ahargunyllib.growth.usecase.point.UpdatePointAccountUseCase
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class ClaimMissionRewardUseCase @Inject constructor(
    private val missionRepository: MissionRepository,
    private val getMyPointAccountUseCase: GetMyPointAccountUseCase,
    private val updatePointAccountUseCase: UpdatePointAccountUseCase,
    private val createPointPostingUseCase: CreatePointPostingUseCase
) {
    suspend operator fun invoke(missionId: String): Resource<Boolean> {
        // Variables to track state for rollback
        var originalAccount: PointAccount? = null
        var balanceUpdated = false

        try {
            // Step 1: Get mission completion
            val completionResult = missionRepository.getMissionCompletion(missionId)

            when (completionResult) {
                is Resource.Success -> {
                    val completion = completionResult.data
                        ?: return Resource.Error("Mission not completed yet")

                    // Check if already claimed
                    if (completion.claimed) {
                        return Resource.Error("Reward already claimed")
                    }

                    // Step 2: Get current point account
                    val pointAccountResult = getMyPointAccountUseCase()
                    if (pointAccountResult !is Resource.Success || pointAccountResult.data == null) {
                        return Resource.Error(pointAccountResult.message ?: "Gagal mengambil akun poin")
                    }

                    val currentAccount = pointAccountResult.data
                    originalAccount = currentAccount.copy() // Save for rollback
                    Log.d("ClaimMissionRewardUseCase", "Current point balance: ${currentAccount.balance}")

                    // Step 3: Update point account balance (add reward points)
                    val updatedAccount = currentAccount.copy(
                        balance = currentAccount.balance + completion.rewardPoints
                    )

                    val updateAccountResult = updatePointAccountUseCase(updatedAccount)
                    if (updateAccountResult is Resource.Error) {
                        return Resource.Error(updateAccountResult.message ?: "Gagal memperbarui poin")
                    }

                    balanceUpdated = true
                    Log.d("ClaimMissionRewardUseCase", "Point balance updated: ${updatedAccount.balance}")

                    // Step 4: Create point posting to record the transaction
                    val pointResult = createPointPostingUseCase(
                        delta = completion.rewardPoints,
                        refType = PointPostingRefType.MISSION_COMPLETION
                    )

                    when (pointResult) {
                        is Resource.Success -> {
                            Log.d("ClaimMissionRewardUseCase", "Point posting created successfully")
                        }
                        is Resource.Error -> {
                            Log.e("ClaimMissionRewardUseCase", "Failed to create point posting: ${pointResult.message}")

                            // ROLLBACK: Restore original balance
                            if (balanceUpdated && originalAccount != null) {
                                Log.d("ClaimMissionRewardUseCase", "Rolling back point balance")
                                updatePointAccountUseCase(originalAccount)
                            }

                            return Resource.Error(pointResult.message ?: "Gagal mencatat transaksi poin")
                        }
                        is Resource.Loading -> {
                            Log.d("ClaimMissionRewardUseCase", "Point posting creation in progress...")
                        }
                    }

                    // Step 5: Update completion to mark as claimed
                    val updatedCompletion = completion.copy(claimed = true)
                    val updateResult = missionRepository.updateMissionCompletion(updatedCompletion)

                    return when (updateResult) {
                        is Resource.Success -> {
                            Log.d("ClaimMissionRewardUseCase", "Claimed ${completion.rewardPoints} points for mission $missionId")
                            Resource.Success(true)
                        }
                        is Resource.Error -> {
                            Log.e("ClaimMissionRewardUseCase", "Failed to update completion: ${updateResult.message}")

                            // ROLLBACK: Restore original balance
                            if (balanceUpdated && originalAccount != null) {
                                Log.d("ClaimMissionRewardUseCase", "Rolling back point balance due to completion update failure")
                                updatePointAccountUseCase(originalAccount)
                            }

                            Resource.Error(updateResult.message ?: "Gagal memperbarui status klaim")
                        }
                        is Resource.Loading -> Resource.Loading()
                    }
                }
                is Resource.Error -> {
                    return Resource.Error(completionResult.message ?: "Failed to get completion")
                }
                is Resource.Loading -> {
                    return Resource.Loading()
                }
            }
        } catch (e: Exception) {
            Log.e("ClaimMissionRewardUseCase", "Error: ${e.message}", e)

            // ROLLBACK on unexpected exception
            if (balanceUpdated && originalAccount != null) {
                Log.d("ClaimMissionRewardUseCase", "Exception occurred - rolling back point balance")
                try {
                    updatePointAccountUseCase(originalAccount)
                } catch (rollbackError: Exception) {
                    Log.e("ClaimMissionRewardUseCase", "CRITICAL: Failed to rollback: ${rollbackError.message}")
                }
            }

            return Resource.Error(e.message ?: "Failed to claim mission reward")
        }
    }
}
