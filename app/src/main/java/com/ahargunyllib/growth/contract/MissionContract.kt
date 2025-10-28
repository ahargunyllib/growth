package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.Mission
import com.ahargunyllib.growth.model.MissionCompletion
import com.ahargunyllib.growth.model.MissionProgress
import com.ahargunyllib.growth.utils.Resource

interface MissionRepository {
    suspend fun getAllMyMission(): Resource<List<Mission>>
    suspend fun createMissionProgress(missionProgress: MissionProgress): Resource<Boolean>
    suspend fun updateMissionProgress(missionProgress: MissionProgress): Resource<Boolean>
    suspend fun createMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean>
    suspend fun updateMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean>
}