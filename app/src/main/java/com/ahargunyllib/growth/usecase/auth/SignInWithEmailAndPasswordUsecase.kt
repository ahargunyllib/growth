package com.ahargunyllib.growth.usecase.auth

import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class SignInWithEmailAndPasswordUsecase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        if (email.isBlank()) {
            return Resource.Error("Email tidak boleh kosong")
        }

        if (password.length < 6) {
            return Resource.Error("Password harus lebih dari 6 karakter")
        }

        return authRepository.signInWithEmailAndPassword(email, password)
    }
}