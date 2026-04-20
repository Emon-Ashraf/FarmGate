package com.example.farmgate.data.remote.dto.issue


data class CreateIssueReportDto(
    val orderId: Long,
    val title: String,
    val description: String
)