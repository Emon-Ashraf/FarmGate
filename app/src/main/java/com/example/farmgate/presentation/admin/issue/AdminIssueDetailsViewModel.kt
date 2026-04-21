package com.example.farmgate.presentation.admin.issue


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminIssueDetailsViewModel(
    private val adminRepository: AdminRepository,
    issueId: Long,
    orderId: Long,
    title: String,
    status: IssueStatus,
    customerName: String,
    farmerName: String,
    createdAt: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminIssueDetailsUiState(
            issueId = issueId,
            orderId = orderId,
            title = title,
            status = status,
            customerName = customerName,
            farmerName = farmerName,
            createdAt = createdAt
        )
    )
    val uiState: StateFlow<AdminIssueDetailsUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    fun onStatusChanged(status: IssueStatus) {
        _uiState.value = _uiState.value.copy(
            selectedStatus = status,
            errorMessage = null
        )
    }

    fun onAdminNoteChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            adminNote = value,
            errorMessage = null
        )
    }

    fun submit() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = adminRepository.resolveIssue(
                    issueId = state.issueId,
                    status = state.selectedStatus,
                    adminNote = state.adminNote.trim().ifBlank { null }
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        successMessage = "Issue updated successfully."
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
        private val adminRepository: AdminRepository,
        private val issueId: Long,
        private val orderId: Long,
        private val title: String,
        private val status: IssueStatus,
        private val customerName: String,
        private val farmerName: String,
        private val createdAt: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminIssueDetailsViewModel(
                adminRepository = adminRepository,
                issueId = issueId,
                orderId = orderId,
                title = title,
                status = status,
                customerName = customerName,
                farmerName = farmerName,
                createdAt = createdAt
            ) as T
        }
    }
}