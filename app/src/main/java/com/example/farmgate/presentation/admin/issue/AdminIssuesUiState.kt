package com.example.farmgate.presentation.admin.issue


import com.example.farmgate.data.model.AdminIssueListItem

data class AdminIssuesUiState(
    val isLoading: Boolean = false,
    val issues: List<AdminIssueListItem> = emptyList(),
    val errorMessage: String? = null
)