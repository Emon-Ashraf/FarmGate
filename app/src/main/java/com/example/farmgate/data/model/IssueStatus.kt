package com.example.farmgate.data.model


enum class IssueStatus {
    Open,
    UnderReview,
    Resolved,
    Rejected;

    companion object {
        fun fromInt(value: Int): IssueStatus {
            return when (value) {
                1 -> Open
                2 -> UnderReview
                3 -> Resolved
                4 -> Rejected
                else -> Open
            }
        }
    }
}