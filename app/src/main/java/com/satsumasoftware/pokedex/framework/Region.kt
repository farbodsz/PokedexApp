package com.satsumasoftware.pokedex.framework

class Region {

    val id: Int
    val name: String

    constructor(id: Int) {
        this.id = id
        this.name = findName(id)
    }

    private fun findName(id: Int) = when (id) {
        0 -> "Not a region"
        1 -> "Kanto"
        2 -> "Johto"
        3 -> "Hoenn"
        4 -> "Sinnoh"
        5 -> "Unova"
        6 -> "Kalos"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}