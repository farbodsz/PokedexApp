package com.satsumasoftware.pokedex.util

import android.text.TextUtils

const val deprecationUseDb: String = "Instead use values from the relevant DB tables and objects"

fun formatPokemonId(id: Int): String {
    val strNum = id.toString()

    return when (strNum.length) {
        1 -> "00" + strNum
        2 -> "0" + strNum
        else -> strNum
    }
}

fun unformatPokemonId(formattedId: String): String {
    if (!TextUtils.isDigitsOnly(formattedId)) {
        return formattedId
    }

    var changeable = formattedId
    while (changeable.startsWith("0")) {
        changeable = changeable.replace("0", "")
    }
    return changeable
}


fun romanToGenId(romanNumerals: String): Int {
    return when (romanNumerals.toUpperCase()) {
        "I" -> 1
        "II" -> 2
        "III" -> 3
        "IV" -> 4
        "V" -> 5
        "VI" -> 6
        else -> throw IllegalArgumentException("invalid roman numeral '$romanNumerals'")
    }
}

fun genIdToRoman(generationId: Int): String {
    return when (generationId) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        6 -> "VI"
        else -> throw IllegalArgumentException("invalid generation id '$generationId'")
    }
}


@Deprecated(deprecationUseDb)
fun growthNameToId(growthRate: String): Int {
    return when (growthRate.toLowerCase()) {
        "fluctuating" -> 6
        "slow" -> 1
        "medium slow" -> 4
        "medium fast" -> 2
        "fast" -> 3
        "erratic" -> 5
        else -> throw IllegalArgumentException("invalid growth rate '$growthRate'")
    }
}

@Deprecated(deprecationUseDb)
fun growthIdToName(id: Int): String {
    return when (id) {
        6 -> "Fluctuating"
        1 -> "Slow"
        4 -> "Medium Slow"
        2 -> "Medium Fast"
        3 -> "Fast"
        5 -> "Erratic"
        else -> throw IllegalArgumentException("invalid growth id '$id'")
    }
}

@Deprecated(deprecationUseDb)
fun growthAbbrToFullName(abbreviation: String): String {
    return when (abbreviation.toLowerCase()) {
        "fl" -> "Fluctuating"
        "s" -> "Slow"
        "ms" -> "Medium Slow"
        "mf" -> "Medium Fast"
        "f" -> "Fast"
        "e" -> "Erratic"
        else -> throw IllegalArgumentException("invalid abbreviation '$abbreviation'")
    }
}


@Deprecated(deprecationUseDb)
fun typeNameToId(typeName: String): Int {
    return when (typeName.toLowerCase()) {
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
        else -> throw IllegalArgumentException("invalid type name '$typeName'")
    }
}


@Deprecated(deprecationUseDb)
fun colourNameToId(colour: String): Int {
    return when (colour.toLowerCase()) {
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
        else -> throw IllegalArgumentException("invalid colour name '$colour'")
    }
}


@Deprecated(deprecationUseDb)
fun shapeNameToId(shape: String): Int {
    return when (shape) {
        "Pomaceous", "Ball" -> 1
        "Caudal", "Squiggle" -> 2
        "Ichthyic", "Fish" -> 3
        "Brachial", "Arms" -> 4
        "Alvine", "Blob" -> 5
        "Sciurine", "Upright" -> 6
        "Crural", "Legs" -> 7
        "Mensal", "Quadruped" -> 8
        "Alar", "Wings" -> 9
        "Cilial", "Tentacles" -> 10
        "Polycephalic", "Heads" -> 11
        "Anthropomorphic", "Humanoid" -> 12
        "Lepidopterous", "Bug wings" -> 13
        "Chitinous", "Armor" -> 14
        else -> throw IllegalArgumentException("invalid shape name '$shape'")
    }
}

@Deprecated(deprecationUseDb)
fun getTechnicalShapeFromSimple(simpleShapeName: String): String {
    return when (simpleShapeName) {
        "Ball" -> "Pomaceous"
        "Squiggle" -> "Caudal"
        "Fish" -> "Ichthyic"
        "Arms" -> "Brachial"
        "Blob" -> "Alvine"
        "Upright" -> "Sciurine"
        "Legs" -> "Crural"
        "Quadruped" -> "Mensal"
        "Wings" -> "Alar"
        "Tentacles" -> "Cilial"
        "Heads" -> "Polycephalic"
        "Humanoid" -> "Anthropomorphic"
        "Bug wings" -> "Lepidopterous"
        "Armor" -> "Chitinous"
        else -> throw IllegalArgumentException("invalid simple shape name '$simpleShapeName'")
    }
}


@Deprecated(deprecationUseDb)
fun habitatNameToId(habitat: String): Int {
    // Habitats are something only in Pokemon FireRed and LeafGreen
    return when (habitat) {
        "Cave" -> 1
        "Forest" -> 2
        "Grassland" -> 3
        "Mountain" -> 4
        "Rare" -> 5
        "Rough Terrain" -> 6
        "Sea" -> 7
        "Urban" -> 8
        "Water's Edge" -> 9
        else -> throw IllegalArgumentException("invalid habitat name '$habitat'")
    }
}
