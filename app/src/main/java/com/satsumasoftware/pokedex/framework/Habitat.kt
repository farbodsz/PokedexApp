package com.satsumasoftware.pokedex.framework

class Habitat {

    // Habitats are something only in Pokemon FireRed and LeafGreen

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
        "cave" -> 1
        "forest" -> 2
        "grassland" -> 3
        "mountain" -> 4
        "rare" -> 5
        "rough terrain" -> 6
        "sea" -> 7
        "urban" -> 8
        "water's edge" -> 9
        else -> throw IllegalArgumentException("invalid habitat name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Cave"
        2 -> "Forest"
        3 -> "Grassland"
        4 -> "Mountain"
        5 -> "Rare"
        6 -> "Rough Terrain"
        7 -> "Sea"
        8 -> "Urban"
        9 -> "Water's Edge"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
