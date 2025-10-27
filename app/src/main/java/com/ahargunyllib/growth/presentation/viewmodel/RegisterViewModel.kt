package com.ahargunyllib.growth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.usecase.auth.SignUpWithEmailAndPasswordUsecase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val resource: Resource<User> = Resource.Loading()
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpWithEmailAndPasswordUsecase: SignUpWithEmailAndPasswordUsecase
) : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun onNameChange(newName: String) {
        _registerState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _registerState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _registerState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _registerState.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun togglePasswordVisibility() {
        _registerState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _registerState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }


    fun signUpWithEmailAndPassword() {
        val currentState = _registerState.value

        if (currentState.password != currentState.confirmPassword) {
            _registerState.update {
                it.copy(resource = Resource.Error("Password tidak cocok"))
            }
            return
        }

        viewModelScope.launch {
            _registerState.update { it.copy(resource = Resource.Loading()) }

            val result = signUpWithEmailAndPasswordUsecase(
                email = currentState.email,
                password = currentState.password,
                name = currentState.name
            )

            _registerState.update {
                it.copy(
                    resource = result
                )
            }
        }
    }

    fun signUpWithGoogle(){
        viewModelScope.launch {
            _registerState.update { it.copy(resource = Resource.Error("Google Sign In is not implemented yet"))  }
        }
    }
}