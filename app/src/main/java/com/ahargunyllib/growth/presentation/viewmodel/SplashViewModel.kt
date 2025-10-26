package com.ahargunyllib.growth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.contract.AuthRepository
import com.ahargunyllib.growth.model.User
import com.ahargunyllib.growth.usecase.auth.SignInWithEmailAndPasswordUsecase
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashState(
    val resource: Resource<User?> = Resource.Loading()
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _splashState = MutableStateFlow(SplashState())
    val splashState = _splashState.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _splashState.update { it.copy(resource = Resource.Loading()) }

            val result = authRepository.getCurrentSession()

            _splashState.update { it.copy(resource = result) }
        }
    }
}