package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.AdminIssueListItem
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.data.remote.dto.admin.AdminIssueListItemDto

fun AdminIssueListItemDto.toModel(): AdminIssueListItem {
    return AdminIssueListItem(
        id = id,
        orderId = orderId,
        title = title,
        status = IssueStatus.fromInt(status),
        customerName = customerName,
        farmerName = farmerName,
        createdAt = createdAt
    )
}