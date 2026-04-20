package com.example.farmgate.data.remote.dto.issue


data class IssueReportDto(
    val id: Long,
    val orderId: Long,
    val customerId: Long,
    val farmerId: Long,
    val title: String,
    val description: String,
    val status: Int,
    val adminNote: String?,
    val createdAt: String
)