package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.usecase.mission.GetAllMyMissionsUseCase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AchievementState(
    val missions: List<MissionWithProgress> = emptyList(),
    val isMissionsLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val getAllMyMissionsUseCase: GetAllMyMissionsUseCase,
) : ViewModel() {
    companion object {
        private const val TAG = "AchievementViewModel"
    }

    private val _state = MutableStateFlow(AchievementState())
    val state = _state.asStateFlow()

    init {
        loadMissions()
    }


    fun loadMissions() {
        viewModelScope.launch {
            _state.update { it.copy(isMissionsLoading = true) }

            when (val result = getAllMyMissionsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            missions = result.data ?: emptyList(),
                            isMissionsLoading = false
                        )
                    }
                    Log.d(TAG, "Missions loaded: ${result.data?.size}")
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isMissionsLoading = false
                        )
                    }
                    Log.e(TAG, "Failed to load missions: ${result.message}")
                }
                is Resource.Loading -> {
                    // Already loading
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun refreshData() {
        loadMissions()
    }
}