package com.ahargunyllib.growth.usecase.auth

import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.utils.Resource
import javax.inject.Inject

class SignUpWithEmailAndPasswordUsecase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): Resource<User> {
        if (name.isBlank()) {
            return Resource.Error("Nama tidak boleh kosong")
        }

        if (email.isBlank()) {
            return Resource.Error("Email tidak boleh kosong")
        }

        if (password.length < 6) {
            return Resource.Error("Password harus minimal 6 karakter")
        }

        return authRepository.signUpWithEmailAndPassword(email, password, name)
    }
}