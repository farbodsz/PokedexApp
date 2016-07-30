package com.satsumasoftware.pokedex.framework

class EggGroup(val id: Int) {

    val name: String

    init {
        name = findName(id)
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Monster"
        2 -> "Water 1"
        3 -> "Bug"
        4 -> "Flying"
        5 -> "Field"
        6 -> "Fairy"
        7 -> "Grass"
        8 -> "Human-Like"
        9 -> "Water 3"
        10 -> "Mineral"
        11 -> "Amorphous"
        12 -> "Water 2"
        13 -> "Ditto"
        14 -> "Dragon"
        15 -> "Undiscovered"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
