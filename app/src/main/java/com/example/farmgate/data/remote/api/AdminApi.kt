package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.admin.AdminIssueListItemDto
import com.example.farmgate.data.remote.dto.admin.AdminResolveIssueDto
import com.example.farmgate.data.remote.dto.issue.IssueReportDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AdminApi {

    @GET("api/admin/issues/open")
    suspend fun getOpenIssues(): Response<List<AdminIssueListItemDto>>

    @PATCH("api/admin/issues/{id}/resolve")
    suspend fun resolveIssue(
        @Path("id") issueId: Long,
        @Body request: AdminResolveIssueDto
    ): Response<IssueReportDto>
}