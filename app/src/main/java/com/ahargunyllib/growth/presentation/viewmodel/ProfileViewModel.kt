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

data class ProfileState(
    val resource: Resource<User?> = Resource.Loading()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _profileState.update { it.copy(resource = Resource.Loading()) }

            val result = authRepository.getCurrentSession()

            _profileState.update { it.copy(resource = result) }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}