package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.AdminIssueListItem
import com.example.farmgate.data.model.IssueReport
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.data.remote.api.AdminApi
import com.example.farmgate.data.remote.dto.admin.AdminResolveIssueDto
import com.example.farmgate.data.remote.mapper.toModel

class AdminRepository(
    private val adminApi: AdminApi
) {

    suspend fun getOpenIssues(): Resource<List<AdminIssueListItem>> {
        return safeApiCall(
            apiCall = { adminApi.getOpenIssues() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }

    suspend fun resolveIssue(
        issueId: Long,
        status: IssueStatus,
        adminNote: String?
    ): Resource<IssueReport> {
        return safeApiCall(
            apiCall = {
                adminApi.resolveIssue(
                    issueId = issueId,
                    request = AdminResolveIssueDto(
                        status = status.toBackendValue(),
                        adminNote = adminNote
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    private fun IssueStatus.toBackendValue(): Int {
        return when (this) {
            IssueStatus.Open -> 1
            IssueStatus.UnderReview -> 2
            IssueStatus.Resolved -> 3
            IssueStatus.Rejected -> 4
        }
    }
}