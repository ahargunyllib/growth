package com.ahargunyllib.growth.usecase.exchange

import com.ahargunyllib.growth.contract.ExchangeRepository
import com.ahargunyllib.growth.model.ExchangeMethod
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetAvailableExchangeMethodsUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepository
) {
    suspend operator fun invoke(): Resource<List<ExchangeMethod>> {
        return exchangeRepository.getAvailableExchangeMethods()
    }
}
