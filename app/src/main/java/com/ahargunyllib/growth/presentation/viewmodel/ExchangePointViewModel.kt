package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.ExchangeMethod
import com.ahargunyllib.growth.model.ExchangeMethodType
import com.ahargunyllib.growth.model.ExchangeTransaction
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.model.PointPosting
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.usecase.exchange.GetAvailableExchangeMethodsUseCase
import com.ahargunyllib.growth.usecase.exchange.InitiateExchangeUseCase
import com.ahargunyllib.growth.usecase.point.CreatePointPostingUseCase
import com.ahargunyllib.growth.usecase.point.GetMyPointAccountUseCase
import com.ahargunyllib.growth.usecase.point.UpdatePointAccountUseCase
import com.ahargunyllib.growth.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExchangePointState(
    val isLoading: Boolean = false,
    val currentPoints: Int = 0,
    val exchangeMethods: List<ExchangeMethod> = emptyList(),
    val selectedMethod: ExchangeMethod? = null,
    val pointsToExchange: String = "",
    val accountNumber: String = "",
    val accountName: String = "",
    val error: String? = null,
    val showConfirmationDialog: Boolean = false,
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val transactionId: String? = null,
    val amountReceived: Int = 0
)

@HiltViewModel
class ExchangePointViewModel @Inject constructor(
    private val getAvailableExchangeMethodsUseCase: GetAvailableExchangeMethodsUseCase,
    private val initiateExchangeUseCase: InitiateExchangeUseCase,
    private val getMyPointAccountUseCase: GetMyPointAccountUseCase,
    private val updatePointAccountUseCase: UpdatePointAccountUseCase,
    private val createPointPostingUseCase: CreatePointPostingUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    companion object {
        private const val TAG = "ExchangePointViewModel"
    }

    private val _state = MutableStateFlow(ExchangePointState())
    val state = _state.asStateFlow()

    init {
        loadExchangeMethods()
        loadPointBalance()
    }

    private fun loadExchangeMethods() {
        viewModelScope.launch {
            try {
                val result = getAvailableExchangeMethodsUseCase()

                when (result) {
                    is Resource.Success -> {
                        val methods = result.data ?: emptyList()
                        _state.update { it.copy(exchangeMethods = methods) }
                        Log.d(TAG, "Loaded ${methods.size} exchange methods")
                    }

                    is Resource.Error -> {
                        Log.e(TAG, "Failed to load exchange methods: ${result.message}")
                        _state.update {
                            it.copy(error = result.message ?: "Gagal memuat metode penukaran")
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading exchange methods: ${e.message}", e)
                _state.update {
                    it.copy(error = "Terjadi kesalahan saat memuat data")
                }
            }
        }
    }

    fun loadPointBalance() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                val result = getMyPointAccountUseCase()

                when (result) {
                    is Resource.Success -> {
                        val balance = result.data?.balance ?: 0
                        _state.update {
                            it.copy(
                                isLoading = false,
                                currentPoints = balance,
                                error = null
                            )
                        }
                        Log.d(TAG, "Point balance: $balance")
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Gagal memuat saldo poin"
                            )
                        }
                        Log.e(TAG, "Failed to load point balance: ${result.message}")
                    }

                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading point balance: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Terjadi kesalahan saat memuat saldo poin"
                    )
                }
            }
        }
    }

    fun selectMethod(method: ExchangeMethod) {
        _state.update { it.copy(selectedMethod = method) }
        Log.d(TAG, "Selected method: ${method.name}")
    }

    fun updatePointsToExchange(points: String) {
        // Only allow digits
        val filtered = points.filter { it.isDigit() }
        _state.update { it.copy(pointsToExchange = filtered, error = null) }
    }

    fun updateAccountNumber(number: String) {
        _state.update { it.copy(accountNumber = number, error = null) }
    }

    fun updateAccountName(name: String) {
        _state.update { it.copy(accountName = name, error = null) }
    }

    fun validateAndShowConfirmation() {
        val currentState = _state.value
        val selectedMethod = currentState.selectedMethod
        val pointsToExchange = currentState.pointsToExchange.toIntOrNull() ?: 0
        val accountNumber = currentState.accountNumber.trim()
        val accountName = currentState.accountName.trim()

        // Validation
        when {
            selectedMethod == null -> {
                _state.update { it.copy(error = "Pilih metode penukaran terlebih dahulu") }
                return
            }

            pointsToExchange <= 0 -> {
                _state.update { it.copy(error = "Masukkan jumlah poin yang valid") }
                return
            }

            pointsToExchange < selectedMethod.minAmount -> {
                _state.update {
                    it.copy(error = "Minimal penukaran adalah ${selectedMethod.minAmount} poin")
                }
                return
            }

            pointsToExchange > selectedMethod.maxAmount -> {
                _state.update {
                    it.copy(error = "Maksimal penukaran adalah ${selectedMethod.maxAmount} poin")
                }
                return
            }

            pointsToExchange > currentState.currentPoints -> {
                _state.update { it.copy(error = "Poin tidak mencukupi") }
                return
            }

            accountNumber.isBlank() -> {
                _state.update { it.copy(error = "Masukkan nomor rekening/akun") }
                return
            }

            accountName.isBlank() -> {
                _state.update { it.copy(error = "Masukkan nama pemilik rekening/akun") }
                return
            }
        }

        // Validate effective points after admin fee
        val effectivePoints = pointsToExchange - selectedMethod.adminFee
        if (effectivePoints <= 0) {
            _state.update {
                it.copy(error = "Jumlah setelah biaya admin harus lebih dari 0 poin")
            }
            return
        }

        // Show confirmation dialog
        _state.update { it.copy(showConfirmationDialog = true, error = null) }
    }

    fun hideConfirmationDialog() {
        _state.update { it.copy(showConfirmationDialog = false) }
    }

    fun processExchange() {
        val currentState = _state.value
        val selectedMethod = currentState.selectedMethod ?: return
        val pointsToExchange = currentState.pointsToExchange.toIntOrNull() ?: return
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            // Variables to track state for rollback
            var originalAccount: PointAccount? = null
            var balanceUpdated = false
            var postingCreated = false

            try {
                _state.update {
                    it.copy(
                        isProcessing = true,
                        showConfirmationDialog = false,
                        error = null
                    )
                }

                // Calculate amount received using integer arithmetic
                // Apply admin fee in points first, then convert to IDR
                val pointsAfterFee = pointsToExchange - selectedMethod.adminFee
                val amountReceived = pointsAfterFee * selectedMethod.conversionRate

                // Step 1: Get current point account and validate
                val pointAccountResult = getMyPointAccountUseCase()
                if (pointAccountResult !is Resource.Success || pointAccountResult.data == null) {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = pointAccountResult.message ?: "Gagal mengambil akun poin"
                        )
                    }
                    return@launch
                }

                val currentAccount = pointAccountResult.data
                originalAccount = currentAccount.copy() // Save original for rollback
                Log.d(TAG, "Current point balance: ${currentAccount.balance}")

                // Validate sufficient balance
                if (currentAccount.balance < pointsToExchange) {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = "Poin tidak mencukupi"
                        )
                    }
                    return@launch
                }

                // Step 2: Deduct points from balance (ATOMIC OPERATION START)
                val updatedAccount = currentAccount.copy(
                    balance = currentAccount.balance - pointsToExchange
                )

                val updateResult = updatePointAccountUseCase(updatedAccount)
                if (updateResult is Resource.Error) {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = updateResult.message ?: "Gagal memperbarui poin"
                        )
                    }
                    return@launch
                }

                balanceUpdated = true
                Log.d(TAG, "Point balance updated: ${updatedAccount.balance}")

                // Step 3: Create point posting record (negative delta for withdrawal)
                val postingResult = createPointPostingUseCase(
                    delta = -pointsToExchange,
                    refType = PointPostingRefType.WITHDRAWAL
                )

                if (postingResult is Resource.Success) {
                    postingCreated = true
                    Log.d(TAG, "Point posting created successfully")
                } else {
                    Log.e(TAG, "Failed to create point posting: ${postingResult.message}")
                    // Continue - posting is for audit trail, not critical for user experience
                }

                // Step 4: Create exchange transaction (FINAL STEP)
                val transaction = ExchangeTransaction(
                    userId = userId,
                    methodType = selectedMethod.type,
                    accountNumber = currentState.accountNumber.trim(),
                    accountName = currentState.accountName.trim(),
                    pointsExchanged = pointsToExchange,
                    amountReceived = amountReceived,
                    adminFee = selectedMethod.adminFee
                )

                val exchangeResult = initiateExchangeUseCase(transaction)
                if (exchangeResult is Resource.Error) {
                    Log.e(TAG, "Failed to create exchange transaction: ${exchangeResult.message}")

                    // ROLLBACK: Restore original balance
                    if (balanceUpdated && originalAccount != null) {
                        Log.d(TAG, "Rolling back point balance update")
                        val rollbackResult = updatePointAccountUseCase(originalAccount)
                        if (rollbackResult is Resource.Error) {
                            Log.e(TAG, "CRITICAL: Failed to rollback balance: ${rollbackResult.message}")
                            _state.update {
                                it.copy(
                                    isProcessing = false,
                                    error = "Terjadi kesalahan serius. Silakan hubungi customer service."
                                )
                            }
                            return@launch
                        }
                        Log.d(TAG, "Balance rollback successful")
                    }

                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = exchangeResult.message ?: "Gagal menyimpan transaksi penukaran"
                        )
                    }
                    return@launch
                }

                val transactionId = exchangeResult.data
                Log.d(TAG, "Exchange transaction created: $transactionId")

                // Success! All steps completed
                _state.update {
                    it.copy(
                        isProcessing = false,
                        isSuccess = true,
                        transactionId = transactionId,
                        amountReceived = amountReceived,
                        currentPoints = updatedAccount.balance,
                        error = null
                    )
                }

                Log.d(TAG, "Exchange processed successfully")

            } catch (e: Exception) {
                Log.e(TAG, "Failed to process exchange: ${e.message}", e)

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

                _state.update {
                    it.copy(
                        isProcessing = false,
                        error = "Terjadi kesalahan saat memproses penukaran"
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.update {
            ExchangePointState(
                exchangeMethods = it.exchangeMethods,
                currentPoints = it.currentPoints
            )
        }
        loadPointBalance()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
