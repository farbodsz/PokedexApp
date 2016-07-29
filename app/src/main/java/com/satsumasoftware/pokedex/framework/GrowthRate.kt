package com.satsumasoftware.pokedex.framework

class GrowthRate {

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

    fun findMaxExperience() = when (id) {
        1 -> 1250000
        2 -> 1000000
        3 -> 800000
        4 -> 1059860
        5 -> 600000
        6 -> 1640000
        else -> throw IllegalArgumentException("invalid growth rate id '$id'")
    }


    private fun findId(name: String) = when (name.toLowerCase()) {
        "fluctuating" -> 6
        "slow" -> 1
        "medium slow" -> 4
        "medium fast" -> 2
        "fast" -> 3
        "erratic" -> 5
        else -> throw IllegalArgumentException("invalid growth rate name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        6 -> "Fluctuating"
        1 -> "Slow"
        4 -> "Medium Slow"
        2 -> "Medium Fast"
        3 -> "Fast"
        5 -> "Erratic"
        else -> throw IllegalArgumentException("invalid growth id '$id'")
    }

}
