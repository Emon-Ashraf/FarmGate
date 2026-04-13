package com.example.farmgate.core.datastore


data class SessionState(
    val token: String = "",
    val role: String = "",
    val userId: String = "",
    val selectedCityId: String = "",
    val selectedCityName: String = ""
) {
    val isLoggedIn: Boolean
        get() = token.isNotBlank()
}