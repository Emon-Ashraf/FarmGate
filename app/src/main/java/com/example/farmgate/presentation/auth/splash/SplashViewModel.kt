package com.example.farmgate.presentation.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.core.navigation.Routes
import com.example.farmgate.data.model.RoleType
import com.example.farmgate.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val session = sessionManager.sessionFlow.first()

            _uiState.value = SplashUiState(isLoading = false)

            if (!session.isLoggedIn) {
                _navigation.emit(Routes.WELCOME)
                return@launch
            }

            _navigation.emit(
                when (RoleType.fromString(session.role)) {
                    RoleType.Customer -> Routes.CUSTOMER_MAIN
                    RoleType.Farmer -> Routes.FARMER_MAIN
                    RoleType.Admin -> Routes.ADMIN_ISSUES
                }
            )
        }
    }

    class Factory(
        private val sessionManager: SessionManager,
        private val profileRepository: ProfileRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(
                sessionManager = sessionManager,
                profileRepository = profileRepository
            ) as T
        }
    }
}