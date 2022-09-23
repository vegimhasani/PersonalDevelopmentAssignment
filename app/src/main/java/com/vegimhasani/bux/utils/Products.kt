package com.vegimhasani.bux.utils

enum class Products {
    GERMANY30,
    US500,
    EURODOLLAR,
    GOLD,
    DEUTSCHEBANK,
    APPLE;

    val id: String
        get() {
            return when (this) {
                GERMANY30 -> "sb26493"
                US500 -> "sb26496"
                EURODOLLAR -> "sb26502"
                GOLD -> "sb26500"
                DEUTSCHEBANK -> "sb28248"
                APPLE -> "sb26513"
            }
        }

    val friendlyName: String
        get() {
            return when (this) {
                GERMANY30 -> "Germany 50"
                US500 -> "US 500"
                EURODOLLAR -> "Euro-Dollar"
                GOLD -> "Gold"
                DEUTSCHEBANK -> "Deutsche Bank"
                APPLE -> "Apple"
            }
        }
}
