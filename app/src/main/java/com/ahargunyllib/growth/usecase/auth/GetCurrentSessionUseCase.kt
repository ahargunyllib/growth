package com.ahargunyllib.growth.usecase.auth

import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class GetCurrentSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<User?> {
        return authRepository.getCurrentSession()
    }
}
