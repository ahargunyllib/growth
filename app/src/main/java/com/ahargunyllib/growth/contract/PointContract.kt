package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.PointAccount
import com.ahargunyllib.growth.model.PointPosting
import com.ahargunyllib.growth.utils.Resource

interface PointRepository {
    suspend fun getMyPointAccount(): Resource<PointAccount>
    suspend fun updateMyPointAccount(pointAccount: PointAccount): Resource<Boolean>
    suspend fun getAllMyPointPosting(): Resource<List<PointPosting>>
    suspend fun createPointPosting(pointPosting: PointPosting): Resource<Boolean>
}