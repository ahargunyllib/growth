package com.ahargunyllib.growth.usecase.point

import com.ahargunyllib.growth.contract.PointRepository
import com.ahargunyllib.growth.model.PointPosting
import com.ahargunyllib.growth.model.PointPostingRefType
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class CreatePointPostingUseCase @Inject constructor(
    private val pointRepository: PointRepository
) {
    suspend operator fun invoke(
        delta: Int,
        refType: PointPostingRefType
    ): Resource<Boolean> {
        if (delta == 0) {
            return Resource.Error("Delta poin tidak boleh 0")
        }

        val pointPosting = PointPosting(
            delta = delta,
            refType = refType
        )

        return pointRepository.createPointPosting(pointPosting)
    }
}
