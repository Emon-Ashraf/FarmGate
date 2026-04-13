package com.example.farmgate.data.model

enum class RoleType {
    Customer,
    Farmer,
    Admin;

    companion object {
        fun fromInt(value: Int): RoleType {
            return when (value) {
                1 -> Customer
                2 -> Farmer
                3 -> Admin
                else -> Customer
            }
        }

        fun fromString(value: String?): RoleType {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: Customer
        }
    }
}