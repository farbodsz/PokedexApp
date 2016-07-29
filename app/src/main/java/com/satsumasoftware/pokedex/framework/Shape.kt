package com.satsumasoftware.pokedex.framework

class Shape(val id: Int) {

    val simpleName: String
    val technicalName: String

    init {
        simpleName = findName(id, false)
        technicalName = findName(id, true)
    }

    private fun findName(id: Int, technicalTerm: Boolean) = when (id) {
        1 -> if (technicalTerm) "Pomaceous" else "Ball"
        2 -> if (technicalTerm) "Caudal" else "Squiggle"
        3 -> if (technicalTerm) "Ichthyic" else "Fish"
        4 -> if (technicalTerm) "Brachial" else "Arms"
        5 -> if (technicalTerm) "Alvine" else "Blob"
        6 -> if (technicalTerm) "Sciurine" else "Upright"
        7 -> if (technicalTerm) "Crural" else "Legs"
        8 -> if (technicalTerm) "Mensal" else "Quadruped"
        9 -> if (technicalTerm) "Alar" else "Wings"
        10 -> if (technicalTerm) "Cilial" else "Tentacles"
        11 -> if (technicalTerm) "Polycephalic" else "Heads"
        12 -> if (technicalTerm) "Anthropomorphic" else "Humanoid"
        13 -> if (technicalTerm) "Lepidopterous" else "Bug wings"
        14 -> if (technicalTerm) "Chitinous" else "Armor"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
