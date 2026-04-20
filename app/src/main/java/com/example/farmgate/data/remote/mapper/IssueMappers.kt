package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.IssueReport
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.data.remote.dto.issue.IssueReportDto

fun IssueReportDto.toModel(): IssueReport {
    return IssueReport(
        id = id,
        orderId = orderId,
        customerId = customerId,
        farmerId = farmerId,
        title = title,
        description = description,
        status = IssueStatus.fromInt(status),
        adminNote = adminNote,
        createdAt = createdAt
    )
}