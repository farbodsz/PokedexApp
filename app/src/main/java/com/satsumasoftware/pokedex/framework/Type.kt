package com.satsumasoftware.pokedex.framework

class Type {

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
        "normal" -> 1
        "fighting" -> 2
        "flying" -> 3
        "poison" -> 4
        "ground" -> 5
        "rock" -> 6
        "bug" -> 7
        "ghost" -> 8
        "steel" -> 9
        "fire" -> 10
        "water" -> 11
        "grass" -> 12
        "electric" -> 13
        "psychic" -> 14
        "ice" -> 15
        "dragon" -> 16
        "dark" -> 17
        "fairy" -> 18
        "unknown" -> 10001
        "shadow" -> 10002
        else -> throw IllegalArgumentException("invalid type name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Normal"
        2 -> "Fighting"
        3 -> "Flying"
        4 -> "Poison"
        5 -> "Ground"
        6 -> "Rock"
        7 -> "Bug"
        8 -> "Ghost"
        9 -> "Steel"
        10 -> "Fire"
        11 -> "Water"
        12 -> "Grass"
        13 -> "Electric"
        14 -> "Psychic"
        15 -> "Ice"
        16 -> "Dragon"
        17 -> "Dark"
        18 -> "Fairy"
        10001 -> "Unknown"
        10002 -> "Shadow"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
