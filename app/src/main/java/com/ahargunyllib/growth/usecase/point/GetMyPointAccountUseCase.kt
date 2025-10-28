package com.ahargunyllib.growth.usecase.point

import com.ahargunyllib.growth.contract.PointRepository
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetMyPointAccountUseCase @Inject constructor(
    private val pointRepository: PointRepository
) {
    suspend operator fun invoke(): Resource<PointAccount> {
        return pointRepository.getMyPointAccount()
    }
}
