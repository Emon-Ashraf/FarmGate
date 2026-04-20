package com.example.farmgate.data.model


data class AdminIssueListItem(
    val id: Long,
    val orderId: Long,
    val title: String,
    val status: IssueStatus,
    val customerName: String,
    val farmerName: String,
    val createdAt: String
)