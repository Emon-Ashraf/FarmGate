package com.example.farmgate.data.model

enum class UnitType(
    val apiValue: Int,
    val displayName: String
) {
    Piece(1, "Piece"),
    Kg(2, "Kg"),
    Liter(3, "Liter"),
    Dozen(4, "Dozen"),
    Bundle(5, "Bundle");

    companion object {
        fun fromInt(value: Int): UnitType {
            return entries.firstOrNull { it.apiValue == value } ?: Piece
        }

        fun fromApiValue(value: Int): UnitType {
            return entries.firstOrNull { it.apiValue == value } ?: Piece
        }
    }
}