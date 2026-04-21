package com.example.farmgate.presentation.admin.issue


import com.example.farmgate.data.model.IssueStatus

data class AdminIssueDetailsUiState(
    val issueId: Long,
    val orderId: Long,
    val title: String,
    val status: IssueStatus,
    val customerName: String,
    val farmerName: String,
    val createdAt: String,
    val selectedStatus: IssueStatus = status,
    val adminNote: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)