package com.satsumasoftware.pokedex.framework

class HatchCounter(val dbValue: Int) {

    val eggCycles: Int
        get() = dbValue

    val eggSteps: Int
        get() = dbValue * 257

}
