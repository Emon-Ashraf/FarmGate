package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.IssueReport
import com.example.farmgate.data.remote.api.IssuesApi
import com.example.farmgate.data.remote.dto.issue.CreateIssueReportDto
import com.example.farmgate.data.remote.mapper.toModel

class IssueRepository(
    private val issuesApi: IssuesApi
) {

    suspend fun createIssue(
        orderId: Long,
        title: String,
        description: String
    ): Resource<IssueReport> {
        return safeApiCall(
            apiCall = {
                issuesApi.createIssue(
                    CreateIssueReportDto(
                        orderId = orderId,
                        title = title,
                        description = description
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun getMyIssues(): Resource<List<IssueReport>> {
        return safeApiCall(
            apiCall = { issuesApi.getMyIssues() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }
}