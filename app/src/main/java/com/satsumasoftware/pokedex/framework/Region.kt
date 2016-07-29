package com.satsumasoftware.pokedex.framework

class Region {

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

    private fun findId(name: String) = when (name.toLowerCase()) {
        "kanto" -> 1
        "johto" -> 2
        "hoenn" -> 3
        "sinnoh" -> 4
        "unova" -> 5
        "kalos" -> 6
        else -> throw IllegalArgumentException("invalid region name '$name'")
    }

}
