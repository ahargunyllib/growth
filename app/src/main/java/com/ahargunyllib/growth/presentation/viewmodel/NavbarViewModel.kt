package com.ahargunyllib.growth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NavbarViewModel @Inject constructor() : ViewModel() {
    private val _pageState = MutableStateFlow<Int>(0)
    val pageState: StateFlow<Int> get() = _pageState

    fun setPageState(page: Int) {
        _pageState.value = page
    }
}