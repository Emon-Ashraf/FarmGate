package com.example.farmgate.data.model


enum class UnitType {
    Piece,
    Kg,
    Liter,
    Dozen,
    Bundle;

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