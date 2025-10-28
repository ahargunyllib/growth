package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.model.QRScanData
import com.ahargunyllib.growth.usecase.collection.CreateCollectionUseCase
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
    private val createPointPostingUseCase: CreatePointPostingUseCase
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

                val collection = collectionResult.data
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

                Log.d(TAG, "Point balance updated: ${updatedAccount.balance}")

                // Step 4: Create point posting record
                val postingResult = createPointPostingUseCase(
                    delta = scanData.points,
                    refType = PointPostingRefType.DEPOSIT
                )

                if (postingResult is Resource.Error) {
                    Log.e(TAG, "Failed to create point posting: ${postingResult.message}")
                    // Don't fail the whole operation if posting fails
                }

                // Success!
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
                _depositState.update {
                    it.copy(
                        isLoading = false,
                        error = "Terjadi kesalahan saat memproses setoran"
                    )
                }
            }
        }
    }

    fun resetState() {
        _depositState.update { DepositState() }
    }

    fun clearError() {
        _depositState.update { it.copy(error = null) }
    }
}
