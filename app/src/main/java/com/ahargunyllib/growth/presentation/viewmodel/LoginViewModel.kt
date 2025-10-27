package com.ahargunyllib.growth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.usecase.auth.SignInWithEmailAndPasswordUsecase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val resource: Resource<User> = Resource.Loading()
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithEmailAndPasswordUsecase: SignInWithEmailAndPasswordUsecase
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _loginState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _loginState.update { it.copy(password = newPassword) }
    }

    fun togglePasswordVisibility() {
        _loginState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }


    fun signInWithEmailAndPassword() {
        val currentState = _loginState.value

        viewModelScope.launch {
            _loginState.update { it.copy(resource = Resource.Loading()) }

            val result = signInWithEmailAndPasswordUsecase(
                email = currentState.email,
                password = currentState.password,
            )

            _loginState.update {
                it.copy(
                    resource = result
                )
            }
        }
    }

    fun signInWithGoogle(){
        viewModelScope.launch {
            _loginState.update { it.copy(resource = Resource.Error("Google Sign In is not implemented yet"))  }
        }
    }
}