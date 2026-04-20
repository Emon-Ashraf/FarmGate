package com.example.farmgate.data.remote.api


import com.example.farmgate.data.remote.dto.issue.CreateIssueReportDto
import com.example.farmgate.data.remote.dto.issue.IssueReportDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IssuesApi {

    @POST("api/issues")
    suspend fun createIssue(
        @Body request: CreateIssueReportDto
    ): Response<IssueReportDto>

    @GET("api/issues/me")
    suspend fun getMyIssues(): Response<List<IssueReportDto>>
}