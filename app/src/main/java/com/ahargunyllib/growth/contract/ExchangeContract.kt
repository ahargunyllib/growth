package com.ahargunyllib.growth.contract

import com.ahargunyllib.growth.model.ExchangeMethod
import com.ahargunyllib.growth.model.ExchangeTransaction
import com.ahargunyllib.growth.utils.Resource

interface ExchangeRepository {
    suspend fun getAvailableExchangeMethods(): Resource<List<ExchangeMethod>>
    suspend fun initiateExchange(transaction: ExchangeTransaction): Resource<String>
    suspend fun getMyExchangeHistory(): Resource<List<ExchangeTransaction>>
    suspend fun getExchangeById(id: String): Resource<ExchangeTransaction>
}
