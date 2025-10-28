package com.ahargunyllib.growth.usecase.point

import com.ahargunyllib.growth.contract.PointRepository
import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class UpdatePointAccountUseCase @Inject constructor(
    private val pointRepository: PointRepository
) {
    suspend operator fun invoke(pointAccount: PointAccount): Resource<Boolean> {
        if (pointAccount.balance < 0) {
            return Resource.Error("Saldo poin tidak boleh negatif")
        }

        return pointRepository.updateMyPointAccount(pointAccount)
    }
}
