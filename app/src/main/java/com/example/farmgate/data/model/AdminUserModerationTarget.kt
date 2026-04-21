package com.example.farmgate.data.model


data class AdminUserModerationTarget(
    val userId: Long,
    val displayName: String,
    val roleName: String,
    val isActive: Boolean
)