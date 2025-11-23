package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.usecase.auth.GetCurrentSessionUseCase
import com.ahargunyllib.growth.usecase.mission.ClaimMissionRewardUseCase
import com.ahargunyllib.growth.usecase.mission.GetAllMyMissionsUseCase
import com.ahargunyllib.growth.usecase.point.GetMyPointAccountUseCase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val pointAccount: PointAccount? = null,
    val missions: List<MissionWithProgress> = emptyList(),
    val isMissionsLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase,
    private val getMyPointAccountUseCase: GetMyPointAccountUseCase,
    private val getAllMyMissionsUseCase: GetAllMyMissionsUseCase,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        loadUserData()
        loadPointAccount()
        loadMissions()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }

            when (val result = getCurrentSessionUseCase()) {
                is Resource.Success -> {
                    _homeState.update {
                        it.copy(
                            user = result.data,
                            isLoading = false
                        )
                    }
                    Log.d(TAG, "User loaded: ${result.data?.name}")
                }
                is Resource.Error -> {
                    _homeState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                    Log.e(TAG, "Failed to load user : ${result.message}")
                }
                is Resource.Loading -> {
                    // Already loading
                }
            }
        }
    }

    fun loadPointAccount() {
        viewModelScope.launch {
            when (val result = getMyPointAccountUseCase()) {
                is Resource.Success -> {
                    _homeState.update {
                        it.copy(pointAccount = result.data)
                    }
                    Log.d(TAG, "Point account loaded: ${result.data?.balance}")
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load point account: ${result.message}")
                }
                is Resource.Loading -> {
                    // Loading state
                }
            }
        }
    }

    fun loadMissions() {
        viewModelScope.launch {
            _homeState.update { it.copy(isMissionsLoading = true) }

            when (val result = getAllMyMissionsUseCase()) {
                is Resource.Success -> {
                    _homeState.update {
                        it.copy(
                            missions = result.data ?: emptyList(),
                            isMissionsLoading = false
                        )
                    }
                    Log.d(TAG, "Missions loaded: ${result.data?.size}")
                }
                is Resource.Error -> {
                    _homeState.update {
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
        _homeState.update { it.copy(error = null) }
    }

    fun refreshData() {
        loadUserData()
        loadPointAccount()
        loadMissions()
    }
}
