package com.example.farmgate.presentation.auth.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.core.navigation.Graph
import com.example.farmgate.core.navigation.Routes
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

            if (!session.isLoggedIn) {
                _uiState.value = SplashUiState(isLoading = false)
                _navigation.emit(Routes.LOGIN)
                return@launch
            }

            when (val result = profileRepository.getMyProfile()) {
                is Resource.Success -> {
                    val profile = result.data
                    sessionManager.saveSession(
                        token = session.token,
                        role = profile.role.name,
                        userId = profile.userId
                    )
                    _uiState.value = SplashUiState(isLoading = false)
                    _navigation.emit(Routes.graphForRole(profile.role))
                }

                is Resource.Error -> {
                    sessionManager.clearSession()
                    _uiState.value = SplashUiState(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _navigation.emit(Graph.AUTH)
                }

                is Resource.Loading -> {
                    _uiState.value = SplashUiState(isLoading = true)
                }
            }
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