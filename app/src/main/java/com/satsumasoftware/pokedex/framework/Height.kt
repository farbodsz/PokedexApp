package com.satsumasoftware.pokedex.framework

class Height(val dbValue: Int) {

    val displayedValue: Double

    init {
        displayedValue = dbValue / 10.0
    }

}
