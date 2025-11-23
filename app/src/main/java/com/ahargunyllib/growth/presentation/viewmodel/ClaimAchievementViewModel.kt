package com.ahargunyllib.growth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.usecase.mission.ClaimMissionRewardUseCase
import com.ahargunyllib.growth.usecase.mission.GetMissionWithProgressUseCase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClaimAchievementState(
    val mission: MissionWithProgress? = null,
    val isMissionLoading: Boolean = false,
    val isClaiming: Boolean = false,
    val claimedPoints: Int? = null,
    val error: String? = null
)

@HiltViewModel
class ClaimAchievementViewModel @Inject constructor(
    private val getMissionWithProgressUseCase: GetMissionWithProgressUseCase,
    private val claimMissionRewardUseCase: ClaimMissionRewardUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ClaimAchievementViewModel"
    }

    private val _state = MutableStateFlow(ClaimAchievementState())
    val state = _state.asStateFlow()

    fun loadMission(missionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isMissionLoading = true) }

            when (val result = getMissionWithProgressUseCase(missionId)) {
                is Resource.Success -> {
                    val mission = result.data

                    if (mission != null) {
                        _state.update {
                            it.copy(
                                mission = mission,
                                isMissionLoading = false
                            )
                        }
                        Log.d(TAG, "Mission loaded: ${mission.mission.id}")
                    } else {
                        _state.update {
                            it.copy(
                                error = "Mission not found",
                                isMissionLoading = false
                            )
                        }
                        Log.e(TAG, "Mission not found: $missionId")
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isMissionLoading = false
                        )
                    }
                    Log.e(TAG, "Failed to load mission: ${result.message}")
                }
                is Resource.Loading -> {
                    // Already loading
                }
            }
        }
    }

    fun claimReward() {
        val mission = _state.value.mission ?: return
        val missionId = mission.mission.id

        // Guard: Prevent claiming if already claimed
        if (mission.isClaimed) {
            _state.update { it.copy(error = "Reward sudah diklaim") }
            Log.w(TAG, "Attempted to claim already claimed mission: $missionId")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isClaiming = true) }

            val rewardPoints = mission.completion?.rewardPoints

            when (val result = claimMissionRewardUseCase(missionId)) {
                is Resource.Success -> {
                    // Update local state to mark as claimed
                    val updatedMission = mission.copy(
                        completion = mission.completion?.copy(claimed = true)
                    )

                    _state.update {
                        it.copy(
                            mission = updatedMission,
                            isClaiming = false,
                            claimedPoints = rewardPoints
                        )
                    }
                    Log.d(TAG, "Reward claimed successfully: $rewardPoints points")
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isClaiming = false
                        )
                    }
                    Log.e(TAG, "Failed to claim reward: ${result.message}")
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

    fun clearClaimedPoints() {
        _state.update { it.copy(claimedPoints = null) }
    }
}
