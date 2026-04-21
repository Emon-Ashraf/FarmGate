package com.example.farmgate.data.remote.dto.admin


data class AdminIssueListItemDto(
    val id: Long,
    val orderId: Long,
    val title: String,
    val status: Int,
    val customerName: String,
    val farmerName: String,
    val createdAt: String
)