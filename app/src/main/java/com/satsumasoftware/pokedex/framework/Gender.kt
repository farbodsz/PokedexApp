package com.satsumasoftware.pokedex.framework

class Gender(val id: Int) {

    fun getSymbol() = when(id) {
        1 -> "\u2640"  // female
        2 -> "\u2642"  // male
        3 -> "\u26B2"  // genderless
        else -> throw IllegalArgumentException("invalid gender id '$id'")
    }

}
