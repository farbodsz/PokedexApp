package com.satsumasoftware.pokedex.framework

class Color {

    val id: Int
    val name: String

    constructor(id: Int) {
        this.id = id
        this.name = findName(id)
    }

    @Deprecated("Use the constructor taking id instead")
    constructor(name: String) {
        this.id = findId(name)
        this.name = name
    }

    private fun findId(name: String) = when (name.toLowerCase()) {
        "black" -> 1
        "blue" -> 2
        "brown" -> 3
        "grey" -> 4
        "green" -> 5
        "pink" -> 6
        "purple" -> 7
        "red" -> 8
        "white" -> 9
        "yellow" -> 10
        else -> throw IllegalArgumentException("invalid color name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Black"
        2 -> "Blue"
        3 -> "Brown"
        4 -> "Grey"
        5 -> "Green"
        6 -> "Pink"
        7 -> "Purple"
        8 -> "Red"
        9 -> "White"
        10 -> "Yellow"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
