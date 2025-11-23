package com.ahargunyllib.growth.usecase.exchange

import com.ahargunyllib.growth.contract.ExchangeRepository
import com.ahargunyllib.growth.model.ExchangeTransaction
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetMyExchangeHistoryUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepository
) {
    suspend operator fun invoke(): Resource<List<ExchangeTransaction>> {
        return exchangeRepository.getMyExchangeHistory()
    }
}
