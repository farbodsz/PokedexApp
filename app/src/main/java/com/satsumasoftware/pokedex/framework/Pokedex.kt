package com.satsumasoftware.pokedex.framework

class Pokedex {

    val id: Int
    val name: String

    constructor(id: Int) {
        this.id = id
        this.name = findName(id)
    }

    private fun findName(id: Int) = when (id) {
        1 -> "National"
        2 -> "Kanto"
        3 -> "Original Johto"
        4 -> "Original Hoenn"
        5 -> "Original Sinnoh"
        6 -> "Extended Sinnoh"
        7 -> "Updated Johto"
        8 -> "Original Unova"
        9 -> "Updated Unova"
        11 -> "Conquest Gallery"
        12 -> "Central Kalos"
        13 -> "Coastal Kalos"
        14 -> "Mountain Kalos"
        15 -> "New Hoenn"
        else -> throw IllegalArgumentException("invalid pokedex id '$id'")
    }

}
