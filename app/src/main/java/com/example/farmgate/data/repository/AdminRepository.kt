package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.AdminIssueListItem
import com.example.farmgate.data.model.IssueReport
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.data.remote.api.AdminApi
import com.example.farmgate.data.remote.dto.admin.AdminModerateProductDto
import com.example.farmgate.data.remote.dto.admin.AdminModerateUserDto
import com.example.farmgate.data.remote.dto.admin.AdminResolveIssueDto
import com.example.farmgate.data.remote.mapper.toModel
import retrofit2.Response

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

    suspend fun moderateUser(
        userId: Long,
        isActive: Boolean
    ): Resource<Unit> {
        return safeUnitApiCall {
            adminApi.moderateUser(
                userId = userId,
                request = AdminModerateUserDto(isActive = isActive)
            )
        }
    }

    suspend fun moderateProduct(
        productId: Long,
        isActive: Boolean
    ): Resource<Unit> {
        return safeUnitApiCall {
            adminApi.moderateProduct(
                productId = productId,
                request = AdminModerateProductDto(isActive = isActive)
            )
        }
    }

    private fun IssueStatus.toBackendValue(): Int {
        return when (this) {
            IssueStatus.Open -> 1
            IssueStatus.UnderReview -> 2
            IssueStatus.Resolved -> 3
            IssueStatus.Rejected -> 4
        }
    }

    private suspend fun safeUnitApiCall(
        apiCall: suspend () -> Response<Unit>
    ): Resource<Unit> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(
                    message = response.errorBody()?.string().orEmpty().ifBlank { "Request failed." },
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error occurred.")
        }
    }
}