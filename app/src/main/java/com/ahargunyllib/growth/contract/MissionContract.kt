package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.Mission
import com.ahargunyllib.growth.model.MissionCompletion
import com.ahargunyllib.growth.model.MissionProgress
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.utils.Resource

interface MissionRepository {
    suspend fun getAllMyMission(): Resource<List<Mission>>
    suspend fun getAllMyMissionsWithProgress(): Resource<List<MissionWithProgress>>
    suspend fun getMissionWithProgress(missionId: String): Resource<MissionWithProgress?>
    suspend fun getMissionProgress(missionId: String): Resource<MissionProgress?>
    suspend fun createMissionProgress(missionProgress: MissionProgress): Resource<Boolean>
    suspend fun updateMissionProgress(missionProgress: MissionProgress): Resource<Boolean>
    suspend fun getMissionCompletion(missionId: String): Resource<MissionCompletion?>
    suspend fun createMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean>
    suspend fun updateMissionCompletion(missionCompletion: MissionCompletion): Resource<Boolean>
}