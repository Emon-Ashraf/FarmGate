package com.example.farmgate.presentation.customer.issue


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.IssueRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateIssueViewModel(
    private val issueRepository: IssueRepository,
    private val orderId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateIssueUiState())
    val uiState: StateFlow<CreateIssueUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    fun onTitleChanged(value: String) {
        _uiState.value = _uiState.value.copy(title = value, errorMessage = null)
    }

    fun onDescriptionChanged(value: String) {
        _uiState.value = _uiState.value.copy(description = value, errorMessage = null)
    }

    fun submit() {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter issue title.")
            return
        }

        if (state.description.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter issue description.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null, successMessage = null)

            when (
                val result = issueRepository.createIssue(
                    orderId = orderId,
                    title = state.title.trim(),
                    description = state.description.trim()
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        successMessage = "Issue report submitted successfully."
                    )
                    _navigation.emit("back")
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = true)
                }
            }
        }
    }

    class Factory(
        private val issueRepository: IssueRepository,
        private val orderId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateIssueViewModel(issueRepository, orderId) as T
        }
    }
}