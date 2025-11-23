package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.model.QRScanData
import com.ahargunyllib.growth.usecase.collection.CreateCollectionUseCase
import com.ahargunyllib.growth.usecase.mission.CompleteMissionUseCase
import com.ahargunyllib.growth.usecase.mission.GetAllMyMissionsUseCase
import com.ahargunyllib.growth.usecase.mission.UpdateMissionProgressUseCase
import com.ahargunyllib.growth.usecase.point.CreatePointPostingUseCase
import com.ahargunyllib.growth.usecase.point.GetMyPointAccountUseCase
import com.ahargunyllib.growth.usecase.point.UpdatePointAccountUseCase
import com.ahargunyllib.growth.utils.EncryptionUtil
import com.ahargunyllib.growth.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DepositState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val scanData: QRScanData? = null,
    val collection: Collection? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val getMyPointAccountUseCase: GetMyPointAccountUseCase,
    private val updatePointAccountUseCase: UpdatePointAccountUseCase,
    private val createPointPostingUseCase: CreatePointPostingUseCase,
    private val getAllMyMissionsUseCase: GetAllMyMissionsUseCase,
    private val updateMissionProgressUseCase: UpdateMissionProgressUseCase,
    private val completeMissionUseCase: CompleteMissionUseCase
) : ViewModel() {

    companion object {
        private const val ENCRYPTION_KEY = "growth-waste-2024"
        private const val TAG = "DepositViewModel"
    }

    private val _depositState = MutableStateFlow(DepositState())
    val depositState = _depositState.asStateFlow()

    fun decryptAndProcessQRCode(encryptedData: String) {
        viewModelScope.launch {
            try {
                _depositState.update { it.copy(isLoading = true, error = null) }

                // Decrypt the QR code data
                val decryptedJson = EncryptionUtil.decryptData(encryptedData, ENCRYPTION_KEY)
                Log.d(TAG, "Decrypted data: $decryptedJson")

                // Parse JSON to QRScanData
                val gson = Gson()
                val scanData = gson.fromJson(decryptedJson, QRScanData::class.java)

                _depositState.update { it.copy(scanData = scanData, isLoading = false) }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to decrypt QR code: ${e.message}", e)
                _depositState.update {
                    it.copy(
                        isLoading = false,
                        error = "QR Code tidak valid atau telah kedaluwarsa"
                    )
                }
            }
        }
    }

    fun processDeposit() {
        val scanData = _depositState.value.scanData ?: return

        viewModelScope.launch {
            // Variables to track state for rollback
            var collection: Collection? = null
            var originalAccount: com.ahargunyllib.growth.model.PointAccount? = null
            var balanceUpdated = false
            var postingCreated = false

            try {
                _depositState.update { it.copy(isLoading = true, error = null) }

                // Step 1: Create collection record
                val collectionResult = createCollectionUseCase(
                    partnerId = scanData.locationId,
                    totalWeightKg = scanData.weight,
                    receivedPoints = scanData.points
                )

                if (collectionResult is Resource.Error) {
                    _depositState.update {
                        it.copy(
                            isLoading = false,
                            error = collectionResult.message ?: "Gagal membuat record setoran"
                        )
                    }
                    return@launch
                }

                collection = collectionResult.data
                Log.d(TAG, "Collection created: $collection")

                // Step 2: Get current point account
                val pointAccountResult = getMyPointAccountUseCase()
                if (pointAccountResult is Resource.Error) {
                    _depositState.update {
                        it.copy(
                            isLoading = false,
                            error = pointAccountResult.message ?: "Gagal mengambil akun poin"
                        )
                    }
                    return@launch
                }

                val currentAccount = pointAccountResult.data!!
                originalAccount = currentAccount.copy() // Save for rollback
                Log.d(TAG, "Current point balance: ${currentAccount.balance}")

                // Step 3: Update point balance
                val updatedAccount = currentAccount.copy(
                    balance = currentAccount.balance + scanData.points
                )

                val updateResult = updatePointAccountUseCase(updatedAccount)
                if (updateResult is Resource.Error) {
                    _depositState.update {
                        it.copy(
                            isLoading = false,
                            error = updateResult.message ?: "Gagal memperbarui poin"
                        )
                    }
                    return@launch
                }

                balanceUpdated = true
                Log.d(TAG, "Point balance updated: ${updatedAccount.balance}")

                // Step 4: Create point posting record (CRITICAL - required for audit trail)
                val postingResult = createPointPostingUseCase(
                    delta = scanData.points,
                    refType = PointPostingRefType.DEPOSIT
                )

                when (postingResult) {
                    is Resource.Success -> {
                        postingCreated = true
                        Log.d(TAG, "Point posting created successfully")
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Failed to create point posting: ${postingResult.message}")

                        // ROLLBACK: Restore original balance
                        if (balanceUpdated && originalAccount != null) {
                            Log.d(TAG, "Rolling back point balance due to posting failure")
                            val rollbackResult = updatePointAccountUseCase(originalAccount)
                            if (rollbackResult is Resource.Error) {
                                Log.e(TAG, "CRITICAL: Failed to rollback balance: ${rollbackResult.message}")
                            }
                        }

                        _depositState.update {
                            it.copy(
                                isLoading = false,
                                error = postingResult.message ?: "Gagal mencatat transaksi poin"
                            )
                        }
                        return@launch
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Point posting creation in progress...")
                    }
                }

                // Step 5: Update mission progress (CRITICAL - must succeed for transactional integrity)
                val missionUpdateResult = updateMissionProgressSync(scanData.weight.toInt())
                if (!missionUpdateResult) {
                    Log.e(TAG, "Failed to update mission progress")

                    // ROLLBACK: Restore original balance
                    if (balanceUpdated && originalAccount != null) {
                        Log.d(TAG, "Rolling back point balance due to mission update failure")
                        val rollbackResult = updatePointAccountUseCase(originalAccount)
                        if (rollbackResult is Resource.Error) {
                            Log.e(TAG, "CRITICAL: Failed to rollback balance: ${rollbackResult.message}")
                        }
                    }

                    _depositState.update {
                        it.copy(
                            isLoading = false,
                            error = "Gagal memperbarui progres misi"
                        )
                    }
                    return@launch
                }

                // Success! All steps completed
                _depositState.update {
                    it.copy(
                        isLoading = false,
                        collection = collection,
                        isSuccess = true,
                        error = null
                    )
                }

                Log.d(TAG, "Deposit processed successfully")

            } catch (e: Exception) {
                Log.e(TAG, "Failed to process deposit: ${e.message}", e)

                // ROLLBACK on unexpected exception
                if (balanceUpdated && originalAccount != null) {
                    Log.d(TAG, "Exception occurred - rolling back point balance")
                    try {
                        updatePointAccountUseCase(originalAccount)
                        Log.d(TAG, "Exception rollback successful")
                    } catch (rollbackError: Exception) {
                        Log.e(TAG, "CRITICAL: Failed to rollback on exception: ${rollbackError.message}")
                    }
                }

                _depositState.update {
                    it.copy(
                        isLoading = false,
                        error = "Terjadi kesalahan saat memproses setoran"
                    )
                }
            }
        }
    }

    /**
     * Synchronously updates mission progress for deposit-related missions.
     * Returns true if all updates succeed, false if any fail.
     * This is part of the deposit transaction and must succeed for transactional integrity.
     */
    private suspend fun updateMissionProgressSync(depositWeight: Int): Boolean {
        try {
            // Get all missions to find weight-based missions
            val missionsResult = getAllMyMissionsUseCase()

            when (missionsResult) {
                is Resource.Success -> {
                    val missions = missionsResult.data ?: emptyList()
                    Log.d(TAG, "Found ${missions.size} missions to check")

                    // Filter for unclaimed and uncompleted missions
                    // Note: Update ALL unclaimed missions as per product requirements
                    // If you need to filter by category (e.g., only "DEPOSIT" or "WEIGHT" missions),
                    // uncomment the following line and adjust the category name:
                    // val relevantMissions = missions.filter { !it.isClaimed && !it.isCompleted && it.mission.category == "DEPOSIT" }
                    val relevantMissions = missions.filter { !it.isClaimed && !it.isCompleted }

                    Log.d(TAG, "Updating ${relevantMissions.size} unclaimed missions")

                    // Update progress for each relevant mission
                    for (missionWithProgress in relevantMissions) {
                        val missionId = missionWithProgress.mission.id
                        Log.d(TAG, "Updating mission progress: $missionId")

                        // Update mission progress with the deposit weight
                        val progressResult = updateMissionProgressUseCase(
                            missionId = missionId,
                            incrementValue = depositWeight
                        )

                        when (progressResult) {
                            is Resource.Success -> {
                                Log.d(TAG, "Updated mission progress for mission: $missionId")

                                // Check if mission is now completed
                                val currentProgress = missionWithProgress.progress?.progressValue ?: 0
                                val updatedProgressValue = currentProgress + depositWeight
                                val targetValue = missionWithProgress.mission.targetValue

                                if (updatedProgressValue >= targetValue) {
                                    // Complete the mission
                                    val completionResult = completeMissionUseCase(missionId)

                                    when (completionResult) {
                                        is Resource.Success -> {
                                            Log.d(TAG, "Mission completed: $missionId")
                                        }
                                        is Resource.Error -> {
                                            Log.e(TAG, "Failed to complete mission: ${completionResult.message}")
                                            // Mission completion failure is critical
                                            return false
                                        }
                                        is Resource.Loading -> {
                                            Log.d(TAG, "Mission completion in progress...")
                                        }
                                    }
                                }
                            }
                            is Resource.Error -> {
                                Log.e(TAG, "Failed to update mission progress: ${progressResult.message}")
                                // Mission progress update failure is critical
                                return false
                            }
                            is Resource.Loading -> {
                                Log.d(TAG, "Mission progress update in progress...")
                            }
                        }
                    }

                    Log.d(TAG, "All mission progress updates completed successfully")
                    return true
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to get missions: ${missionsResult.message}")
                    return false
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Loading missions...")
                    return false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating mission progress: ${e.message}", e)
            return false
        }
    }

    fun resetState() {
        _depositState.update { DepositState() }
    }

    fun clearError() {
        _depositState.update { it.copy(error = null) }
    }
}
