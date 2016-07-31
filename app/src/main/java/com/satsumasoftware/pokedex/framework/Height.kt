package com.satsumasoftware.pokedex.framework

class Height(val dbValue: Int) {

    val displayedValue: Double

    val displayedText: String
        get() {
            return "$displayedValue m"
        }

    init {
        displayedValue = dbValue / 10.0
    }

}
