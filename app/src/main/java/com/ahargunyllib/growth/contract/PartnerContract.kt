package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.Partner
import com.ahargunyllib.growth.utils.Resource

interface PartnerRepository {
    suspend fun getAllPartners(): Resource<List<Partner>>
    suspend fun getPartnerById(id: String): Resource<Partner>
}