package com.ahargunyllib.growth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.contract.PartnerRepository
import com.ahargunyllib.growth.model.Partner
import com.ahargunyllib.growth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerListViewModel @Inject constructor(
    private val partnerRepository: PartnerRepository
) : ViewModel() {

    private val _partnersState = MutableStateFlow<Resource<List<Partner>>>(Resource.Loading())
    val partnersState: StateFlow<Resource<List<Partner>>> = _partnersState

    fun getAllPartners() {
        viewModelScope.launch {
            _partnersState.value = Resource.Loading()
            val result = partnerRepository.getAllPartners()
            _partnersState.value = result
        }
    }
}