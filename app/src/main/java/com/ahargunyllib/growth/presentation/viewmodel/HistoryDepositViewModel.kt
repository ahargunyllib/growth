package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.usecase.collection.GetAllMyCollectionsUseCase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryDepositState(
    val isLoading: Boolean = false,
    val collections: List<Collection> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false
)

@HiltViewModel
class HistoryDepositViewModel @Inject constructor(
    private val getAllMyCollectionsUseCase: GetAllMyCollectionsUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "HistoryDepositViewModel"
    }

    private val _historyState = MutableStateFlow(HistoryDepositState())
    val historyState = _historyState.asStateFlow()

    init {
        loadCollections()
    }

    fun loadCollections() {
        viewModelScope.launch {
            try {
                _historyState.update { it.copy(isLoading = true, error = null) }

                val result = getAllMyCollectionsUseCase()

                when (result) {
                    is Resource.Success -> {
                        val collections = result.data ?: emptyList()
                        _historyState.update {
                            it.copy(
                                isLoading = false,
                                collections = collections,
                                isEmpty = collections.isEmpty(),
                                error = null
                            )
                        }
                        Log.d(TAG, "Loaded ${collections.size} collections")
                    }

                    is Resource.Error -> {
                        _historyState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Gagal memuat riwayat setoran"
                            )
                        }
                        Log.e(TAG, "Failed to load collections: ${result.message}")
                    }

                    is Resource.Loading -> {
                        _historyState.update { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading collections: ${e.message}", e)
                _historyState.update {
                    it.copy(
                        isLoading = false,
                        error = "Terjadi kesalahan saat memuat data"
                    )
                }
            }
        }
    }

    fun retryLoad() {
        loadCollections()
    }

    fun clearError() {
        _historyState.update { it.copy(error = null) }
    }
}