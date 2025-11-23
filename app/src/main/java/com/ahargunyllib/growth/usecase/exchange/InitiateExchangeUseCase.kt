package com.ahargunyllib.growth.usecase.exchange

import com.ahargunyllib.growth.contract.ExchangeRepository
import com.ahargunyllib.growth.model.ExchangeTransaction
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class InitiateExchangeUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepository
) {
    suspend operator fun invoke(transaction: ExchangeTransaction): Resource<String> {
        return exchangeRepository.initiateExchange(transaction)
    }
}
