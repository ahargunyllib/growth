package com.ahargunyllib.growth.usecase.mission

import com.ahargunyllib.growth.contract.MissionRepository
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetAllMyMissionsUseCase @Inject constructor(
    private val missionRepository: MissionRepository
) {
    suspend operator fun invoke(): Resource<List<MissionWithProgress>> {
        return missionRepository.getAllMyMissionsWithProgress()
    }
}
