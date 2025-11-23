package com.ahargunyllib.growth.usecase.mission

import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetMissionWithProgressUseCase @Inject constructor(
    private val missionRepository: MissionRepository
) {
    suspend operator fun invoke(missionId: String): Resource<MissionWithProgress?> {
        return missionRepository.getMissionWithProgress(missionId)
    }
}
