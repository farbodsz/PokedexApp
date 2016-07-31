package com.satsumasoftware.pokedex.framework

class Mass(val dbValue: Int) {

    val displayedValue: Double

    val displayedText: String
        get() {
            return "$displayedValue kg"
        }

    init {
        displayedValue = dbValue / 10.0
    }

}
