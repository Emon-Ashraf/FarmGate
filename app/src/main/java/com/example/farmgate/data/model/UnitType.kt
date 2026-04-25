package com.example.farmgate.data.model

enum class UnitType(val apiValue: Int) {
    Piece(1),
    Kg(2),
    Liter(3),
    Dozen(4),
    Bundle(5);

    companion object {
        fun fromInt(value: Int): UnitType {
            return when (value) {
                1 -> Piece
                2 -> Kg
                3 -> Liter
                4 -> Dozen
                5 -> Bundle
                else -> Piece
            }
        }
    }
}